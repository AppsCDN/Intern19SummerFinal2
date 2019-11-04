package com.example.intern19summerfinal2.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intern19summerfinal2.R
import com.example.intern19summerfinal2.adapter.MangaAdapter
import com.example.intern19summerfinal2.api.APIClient
import com.example.intern19summerfinal2.model.Manga
import com.example.intern19summerfinal2.model.MangaListResponse
import com.example.intern19summerfinal2.utils.MangaAPI
import kotlinx.android.synthetic.main.activity_manga_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class MangaListActivity : AppCompatActivity() {

    private var mangaAPI: MangaAPI? = null
    var mangaList = mutableListOf<Manga>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_list)

        mangaAPI = APIClient.getClient()
        val call = mangaAPI?.getResponse()
        call?.enqueue(object : Callback<MangaListResponse> {
            override fun onFailure(call: Call<MangaListResponse>, t: Throwable) {
                Toast.makeText(this@MangaListActivity, "Fail", Toast.LENGTH_SHORT).show()
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
                val mangaAdapter = MangaAdapter(mangaList)
                recyclerViewMangaList.layoutManager = LinearLayoutManager(this@MangaListActivity, LinearLayoutManager.VERTICAL, false)
                recyclerViewMangaList.setHasFixedSize(true)
                recyclerViewMangaList.adapter = mangaAdapter
                mangaAdapter.notifyDataSetChanged()
            }
        })
    }
}
