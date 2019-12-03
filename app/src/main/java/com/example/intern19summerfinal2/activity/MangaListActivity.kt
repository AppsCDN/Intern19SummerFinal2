package com.example.intern19summerfinal2.activity

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
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
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_manga_list.*
import kotlinx.android.synthetic.main.activity_manga_list.progressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class MangaListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object{
        private const val CATEGORIES = "CATEGORIES"
        private const val KEY_CATEGORIES = "KEY_CATEGORIES"
    }

    private lateinit var mangaAdapter: MangaAdapter
    private lateinit var searchManager: SearchManager
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var categoriesPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor ?= null

    private lateinit var searchView: SearchView
    private var isSearching = false
    private var searchText: String = ""

    private var mangaAPI: MangaAPI? = null
    private var mangaList = mutableListOf<Manga>()

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_list)

        loadMangaList()
        initSwipeRefreshLayout()
        initDrawerLayout()

        categoriesPreferences = getSharedPreferences(CATEGORIES, Context.MODE_PRIVATE)
        editor = categoriesPreferences.edit()
    }

    private fun loadMangaList() {
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
            override fun onItemClick(item: Manga) { startActivity(MangaInfoFullActivity.newIntent(this@MangaListActivity, item.id ?: "", item.title ?: ""
                    )
                )
            }
        })
        recyclerViewMangaList.layoutManager = LinearLayoutManager(this)
        recyclerViewMangaList.setHasFixedSize(true)
        recyclerViewMangaList.adapter = mangaAdapter
        mangaList.sortWith(Comparator { p0, p1 -> p1.lastChapterDate.formatDate().compareTo(p0.lastChapterDate.formatDate())
        })
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
                val filteredList = mutableListOf<Manga>()
                for (item: Manga in mangaList) {
                    val search = item.title?.toLowerCase()
                    if (search != null) {
                        if (search.contains(newText.toString())) {
                            filteredList.add(item)
                        }
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
        Handler().postDelayed({ swipeRefreshLayout.isRefreshing = false }, 3000)
        mangaAdapter.notifyItemChanged(mangaList.size)
    }

    private fun initDrawerLayout() {
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_open, R.string.navigation_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("WrongConstant")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.all -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.app_name))
                editor?.apply()
                showCategoriesData()
            }
            R.id.adult -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_adult))
                editor?.apply()
                showCategoriesData()
            }
            R.id.adventure -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_adventure))
                editor?.apply()
                showCategoriesData()
            }
            R.id.comedy -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_comedy))
                editor?.apply()
                showCategoriesData()
            }
            R.id.doujinshi -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_doujinshi))
                editor?.apply()
                showCategoriesData()
            }
            R.id.drama -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_drama))
                editor?.apply()
                showCategoriesData()
            }
            R.id.ecchi -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_ecchi))
                editor?.apply()
                showCategoriesData()
            }
            R.id.fantasy -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_fantasy))
                editor?.apply()
                showCategoriesData()
            }
            R.id.gender_bender -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_gender_bender))
                editor?.apply()
                showCategoriesData()
            }
            R.id.harem -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_harem))
                editor?.apply()
                showCategoriesData()
            }
            R.id.historical -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_historical))
                editor?.apply()
                showCategoriesData()
            }
            R.id.horror -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_horror))
                editor?.apply()
                showCategoriesData()
            }
            R.id.josei -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_josei))
                editor?.apply()
                showCategoriesData()
            }
            R.id.mature -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_mature))
                editor?.apply()
                showCategoriesData()
            }
            R.id.mecha -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_mecha))
                editor?.apply()
                showCategoriesData()
            }
            R.id.mystery -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_mystery))
                editor?.apply()
                showCategoriesData()
            }
            R.id.one_shot -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_one_shot))
                editor?.apply()
                showCategoriesData()
            }
            R.id.psychological -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_psychological))
                editor?.apply()
                showCategoriesData()
            }
            R.id.romance -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_romance))
                editor?.apply()
                showCategoriesData()
            }
            R.id.school_life -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_school_life))
                editor?.apply()
                showCategoriesData()
            }
            R.id.seinen -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_seinen))
                editor?.apply()
                showCategoriesData()
            }
            R.id.shoujo -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_shoujo))
                editor?.apply()
                showCategoriesData()
            }
            R.id.shounen -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_shounen))
                editor?.apply()
                showCategoriesData()
            }
            R.id.slice_of_life -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_slice_of_life))
                editor?.apply()
                showCategoriesData()
            }
            R.id.smut -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_smut))
                editor?.apply()
                showCategoriesData()
            }
            R.id.sports -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_sports))
                editor?.apply()
                showCategoriesData()
            }
            R.id.supernatural -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_supernatural))
                editor?.apply()
                showCategoriesData()
            }
            R.id.tragedy -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_tragedy))
                editor?.apply()
                showCategoriesData()
            }
            R.id.webtoons -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_webtoons))
                editor?.apply()
                showCategoriesData()
            }
            R.id.yaoi -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_yaoi))
                editor?.apply()
                showCategoriesData()
            }
            R.id.yuri -> {
                editor?.putString(KEY_CATEGORIES, getString(R.string.menu_categories_yuri))
                editor?.apply()
                showCategoriesData()
            }
        }
        drawerLayout.closeDrawer(Gravity.START)
        return true
    }

    private fun showCategoriesData() {
        val categories = categoriesPreferences.getString(KEY_CATEGORIES, getString(R.string.app_name))
        if (categories == getString(R.string.app_name)) {
            supportActionBar?.title = categories
            mangaAdapter.setCategories(mangaList)
        } else {
            val newList = mutableListOf<Manga>()
            for (list in mangaList) {
                for (value in list.category!!) {
                    if (value == categories) {
                        supportActionBar?.title = value
                        newList.add(list)
                    }
                }
            }
            mangaAdapter.setCategories(newList)
        }
    }
}
