package com.example.intern19summerfinal2.activity

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intern19summerfinal2.R
import com.example.intern19summerfinal2.adapter.MangaAdapter
import com.example.intern19summerfinal2.api.APIClient
import com.example.intern19summerfinal2.model.Manga
import com.example.intern19summerfinal2.model.MangaListResponse
import com.example.intern19summerfinal2.api.MangaAPI
import com.example.intern19summerfinal2.utils.AdapterOnItemClickListener
import com.example.intern19summerfinal2.utils.formatDate
import kotlinx.android.synthetic.main.activity_manga_list.*
import kotlinx.android.synthetic.main.activity_manga_list.progressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class MangaListActivity : AppCompatActivity() {
    private lateinit var mangaAdapter: MangaAdapter
    private lateinit var searchManager: SearchManager

    private lateinit var searchView: SearchView
    private var isSearching = false
    private var searchText: String = ""

    private var mangaAPI: MangaAPI? = null
    private var mangaList = mutableListOf<Manga>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_list)

        initSwipeRefreshLayout()

        mangaAPI = APIClient.getClient()
        val call = mangaAPI?.getResponse()
        call?.enqueue(object : Callback<MangaListResponse> {
            override fun onFailure(call: Call<MangaListResponse>, t: Throwable) {
                Toast.makeText(this@MangaListActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MangaListResponse>, response: Response<MangaListResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()?.manga
                    if (list != null) {
                        for (i in 0 until list.size) {
                            val manga = list[i]
                            mangaList.add(manga)
                        }
                    }
                }
                progressBar.visibility = View.GONE
                initAdapter()
            }
        })
    }

    private fun initAdapter() {
        mangaAdapter = MangaAdapter(mangaList, object : AdapterOnItemClickListener<Manga> {
            override fun onItemClick(item: Manga) {
                startActivity(MangaInfoFullActivity.newIntent(this@MangaListActivity, item.id ?: "", item.title ?: ""))
            }
        })
        recyclerViewMangaList.layoutManager = LinearLayoutManager(this)
        recyclerViewMangaList.setHasFixedSize(true)
        recyclerViewMangaList.adapter = mangaAdapter
        mangaList.sortWith(Comparator { p0, p1 -> p1.lastChapterDate.formatDate().compareTo(p0.lastChapterDate.formatDate()) })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_manga_list, menu)
        val menuItem = menu?.findItem(R.id.action_search)
        searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        Log.d("searchservice", "" + getSystemService(Context.SEARCH_SERVICE))
        searchView = menuItem?.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        if (isSearching) {
            menuItem.expandActionView()
            searchView.setQuery(searchText, true)
            searchView.isIconified = false
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("DefaultLocale")
            override fun onQueryTextChange(text: String?): Boolean {
                val newText = text?.toLowerCase()
                val filteredList= mutableListOf<Manga>()
                for (item: Manga in mangaList) {
                    val search = item.title?.toLowerCase()
                    if (search != null) {
                        if (search.contains(newText.toString()))
                            filteredList.add(item)
                    }
                }
                mangaAdapter.setSearchOperation(filteredList)
                return true
            }
        })

        searchView.setOnSearchClickListener { swipeRefreshLayout.isEnabled = false }

        searchView.setOnCloseListener {
            swipeRefreshLayout.isEnabled = true
            false
        }
        return true
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright)
        swipeRefreshLayout.setOnRefreshListener {
            pullRefresh()
        }
    }

    private fun pullRefresh() {
        swipeRefreshLayout.isRefreshing = false
        mangaAdapter.notifyItemChanged(mangaList.size)
    }
}
