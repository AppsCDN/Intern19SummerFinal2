package com.example.intern19summerfinal2.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intern19summerfinal2.R
import com.example.intern19summerfinal2.adapter.PageAdapter
import com.example.intern19summerfinal2.api.APIClient
import com.example.intern19summerfinal2.api.MangaAPI
import com.example.intern19summerfinal2.model.Page
import com.example.intern19summerfinal2.model.PageListResponse
import kotlinx.android.synthetic.main.activity_chapter_pages.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChapterPagesActivity : AppCompatActivity() {
    private var pageAdapter: PageAdapter ?= null
    private val pageList = arrayListOf<Page>()
    private var mangaAPI: MangaAPI ?= null

    companion object {
        private const val CHAPTER_ID_TAG = "ChapterID"
        private const val CHAPTER_TITLE_TAG = "ChapterTitle"

        private const val ZOOM_ACTIVITY_REQUEST_CODE = 101
        private const val POSITION_TAG = "position"

        fun newIntent(context: Context, chapterID: String, chapterTitle: String): Intent {
            val intent = Intent(context, ChapterPagesActivity::class.java)
            intent.putExtra(CHAPTER_ID_TAG, chapterID)
            intent.putExtra(CHAPTER_TITLE_TAG, chapterTitle)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter_pages)

        val chapterID = intent.extras?.getString(CHAPTER_ID_TAG) ?: ""
        val chapterTitle = intent.extras?.getString(CHAPTER_TITLE_TAG) ?: ""

        supportActionBar?.hide()
        loadPageChapter(chapterID)
    }

    fun loadPageChapter(chapterId: String) {
        mangaAPI = APIClient.getClient()
        val call = mangaAPI?.getPageList(chapterId)
        call?.enqueue(object : Callback<PageListResponse> {
            override fun onFailure(call: Call<PageListResponse>, t: Throwable) {
                Toast.makeText(this@ChapterPagesActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<PageListResponse>, response: Response<PageListResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()?.pages
                    list?.let {
                        for (i in 0 until list.size) {
                            val pages = list[i]
                            val page: Page? = Page()
                            for (j in 0 until pages.size) {
                                val gallery: String = pages[j]
                                when (j) {
                                    0 -> page?.number = gallery.toInt()
                                    1 -> page?.url = gallery
                                    2 -> page?.width = gallery.toInt()
                                    3 -> page?.height = gallery.toInt()
                                }
                            }
                            page?.let { pageList.add(it) }
                        }
                    }
                }
                initAdapter()
            }
        })
    }

    private fun initAdapter() {
        pageAdapter = PageAdapter(pageList, object : PageAdapter.OnItemClickListener {
            override fun onItemClick(page: Page, position: Int) {
                startActivityForResult(ZoomActivity.newIntent(this@ChapterPagesActivity, pageList, position), ZOOM_ACTIVITY_REQUEST_CODE)
            }
        })
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = pageAdapter
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ZOOM_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val position = data?.getIntExtra(POSITION_TAG, 0) ?: 0
                recyclerView.scrollToPosition(position)
            }
        }
    }
}
