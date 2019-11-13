package com.example.intern19summerfinal2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intern19summerfinal2.R
import com.example.intern19summerfinal2.adapter.ChapterAdapter
import com.example.intern19summerfinal2.api.APIClient
import com.example.intern19summerfinal2.api.MangaAPI
import com.example.intern19summerfinal2.model.Chapter
import com.example.intern19summerfinal2.model.MangaFullInfo
import com.example.intern19summerfinal2.utils.AdapterOnItemClickListener
import com.example.intern19summerfinal2.utils.formatDate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_manga_info_full.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNCHECKED_CAST", "DEPRECATION")
class MangaInfoFullActivity : AppCompatActivity() {
    private var chapterAdapter: ChapterAdapter? = null
    private var chapterList = mutableListOf<Chapter>()
    private var mangaAPI: MangaAPI? = null

    companion object {
        private const val IMAGE_URL = "https://cdn.mangaeden.com/mangasimg/"
        const val MANGA_ID_TAG = "MangaID"
        const val MANGA_TITLE_TAG = "MangaTitle"

        fun newIntent(context: Context, mangaID: String, mangaTitle: String): Intent {
            val intent = Intent(context, MangaInfoFullActivity::class.java)
            intent.putExtra(MANGA_ID_TAG, mangaID)
            intent.putExtra(MANGA_TITLE_TAG, mangaTitle)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_info_full)

        val mangaID = intent?.extras?.getString(MANGA_ID_TAG) ?: ""
        val mangaTitle = intent?.extras?.getString(MANGA_TITLE_TAG) ?: ""

        loadMangaID(mangaID)
        initActionBar(mangaTitle)
    }

    private fun initActionBar(mangaTitle: String) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = mangaTitle
    }

    private fun loadMangaID(mangaID: String) {
        mangaAPI = APIClient.getClient()
        val call = mangaAPI?.getMangaFullInfo(mangaID)
        call?.enqueue(object : Callback<MangaFullInfo> {
            override fun onFailure(call: Call<MangaFullInfo>, t: Throwable) {
                Toast.makeText(this@MangaInfoFullActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MangaFullInfo>, response: Response<MangaFullInfo>) {
                if (response.isSuccessful && response.body() != null) {
                    val mangaFullInfo = response.body()
                    mangaFullInfo?.let { initView(it) }
                    Log.d("display", "" + mangaFullInfo?.chapters)

                    val list = mangaFullInfo?.chapters
                    list?.let {
                        for (i in 0 until list.size) {
                            val chapters = list[i]
                            val chapter: Chapter? = Chapter()
                            for (k in 0 until chapters.size) {
                                val value: String? = chapters[k]
                                when (k) {
                                    0 -> chapter?.number = value?.toFloat()
                                    1 -> chapter?.date = value?.split('.')?.get(0)?.toLong()
                                    2 -> chapter?.title = value
                                    3 -> chapter?.id = value
                                }
                            }
                            chapter?.let { chapterList.add(it) }
                        }
                    }
                }
                initAdapter()
            }
        })
    }

    private fun initView(mangaFullInfo: MangaFullInfo) {

        val image = mangaFullInfo.image
        image?.let {
            Picasso.with(this)
                .load(IMAGE_URL + image)
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_noimage)
                .into(imgFullManga)
        }

        tvMangaFullReleasedDate.text = mangaFullInfo.last_chapter_date.formatDate()
        tvMangaFullRatingNumber.text = mangaFullInfo.hits.toString()
        tvMangaFullAuthorName.text = mangaFullInfo.author
        tvMangaFullCategoriesName.text = mangaFullInfo.categoriesAsString
        tvMangaFullDescription.text = Html.fromHtml(mangaFullInfo.description)

        progressBar.visibility = View.GONE
        mangaInfoFullContainer.visibility = View.VISIBLE
    }

    private fun initAdapter() {
        chapterAdapter = ChapterAdapter(chapterList, object : AdapterOnItemClickListener<Chapter> {
            override fun onItemClick(item: Chapter) {
                startActivity(ChapterPagesActivity.newIntent(this@MangaInfoFullActivity, item.id ?: "", item.title ?: ""))
            }
        })
        recyclerViewChapterList.layoutManager = LinearLayoutManager(this)
        recyclerViewChapterList.setHasFixedSize(true)
        recyclerViewChapterList.adapter = chapterAdapter
        ViewCompat.setNestedScrollingEnabled(recyclerViewChapterList, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
