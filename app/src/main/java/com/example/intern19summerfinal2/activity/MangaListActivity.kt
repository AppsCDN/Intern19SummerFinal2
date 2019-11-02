package com.example.intern19summerfinal2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.NetworkOnMainThreadException
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intern19summerfinal2.R
import com.example.intern19summerfinal2.adapter.MangaAdapter
import com.example.intern19summerfinal2.api.APIClient
import com.example.intern19summerfinal2.model.Manga
import com.example.intern19summerfinal2.model.MangaListResponse
import com.example.intern19summerfinal2.utils.MangaAPI
import com.example.intern19summerfinal2.utils.OnItemClickListener
import kotlinx.android.synthetic.main.activity_manga_list.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class MangaListActivity : AppCompatActivity() {
    companion object {
        const val JSON_URL = "https://www.mangaeden.com/"
        const val IMAGE_URL = "https://cdn.mangaeden.com/mangasimg/"
    }

    private var listener: OnItemClickListener<Manga>? = null
    private var mangaAPI: MangaAPI? = null
    var mangaList = mutableListOf<Manga>()
    var mangaItem: Manga? = null
    var list: ArrayList<Manga> ?= null

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
                    val d = Log.d("show", "" + response.body()?.manga)
                    Toast.makeText(this@MangaListActivity, "Success", Toast.LENGTH_SHORT).show()
                    try {
                        val url = URL(JSON_URL)
                        val httpURLConnection = url.openConnection() as HttpURLConnection?
                        httpURLConnection?.connect()
                        Log.d("http", "" + httpURLConnection?.connect())

                        val inputStream = httpURLConnection?.inputStream
                        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                        val stringBuffer = StringBuffer()
                        var line: String?
                        line = bufferedReader.readLine()
                        while (line != null) { stringBuffer.append(line) }

                        val jsonObject = JSONObject(JSON_URL)
                        val jsonObjectManga = jsonObject.getJSONObject("manga")
                        val imageArray = JSONArray(IMAGE_URL + mangaItem?.image)

                        for (i in 0 until jsonObjectManga.length()) {
                            val manga = Manga()
                            val dataObj = jsonObjectManga.getJSONObject(i.toString())
                            with(manga) {
                                alias = dataObj.getString("a")
                                category = dataObj.getString("c") as ArrayList<String>
                                hits = dataObj.getInt("h")
                                id = dataObj.getString("id")
                                image = dataObj.getString("im")
                                lastChapterDate = dataObj.getLong("ld")
                                status = dataObj.getInt("s")
                                title = dataObj.getString("t")
                            }
                            mangaList.add(manga)
                        }
                    } catch (e: NetworkOnMainThreadException) {
                        e.printStackTrace()
                    }
                    val mangaAdapter = listener?.let { MangaAdapter(it) }
                    recyclerViewMangaList.layoutManager = LinearLayoutManager(this@MangaListActivity, LinearLayoutManager.VERTICAL, false)
                    recyclerViewMangaList.setHasFixedSize(true)
                    recyclerViewMangaList.adapter = mangaAdapter
                    mangaAdapter?.notifyDataSetChanged()
                }
            }
        })
    }
}



