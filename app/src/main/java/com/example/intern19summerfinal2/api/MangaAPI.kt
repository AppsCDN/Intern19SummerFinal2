package com.example.intern19summerfinal2.api

import com.example.intern19summerfinal2.model.MangaFullInfo
import com.example.intern19summerfinal2.model.MangaListResponse
import com.example.intern19summerfinal2.model.PageListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MangaAPI {
    @GET("api/list/0")
    fun getResponse(): Call<MangaListResponse>

    @GET("api/manga/{mangaId}/")
    fun getMangaFullInfo(@Path("mangaId") mangaId: String): Call<MangaFullInfo>

    @GET("api/chapter/{chapterId}/")
    fun getPageList(@Path("chapterId") chapterId: String): Call<PageListResponse>
}
