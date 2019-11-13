package com.example.intern19summerfinal2.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import java.lang.reflect.Modifier.*

class APIClient {
    companion object {
        var BASE_URL = "https://www.mangaeden.com/"

        private var retrofit: Retrofit? = null
        fun getClient(): MangaAPI? {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            val okHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

            if (retrofit == null) {
                val builder = GsonBuilder().excludeFieldsWithModifiers(FINAL, TRANSIENT, STATIC)
                val gson = builder.create()
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build()
            }
            return retrofit?.create(MangaAPI::class.java)
        }
    }
}
