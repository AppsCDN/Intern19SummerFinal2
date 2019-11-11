package com.example.intern19summerfinal2.model

data class MangaFullInfo(
    var artist: String?,
    var artist_kw: ArrayList<String>?,
    var author: String?,
    var author_kw: ArrayList<String>?,
    var categories: ArrayList<String>?,
    var chapters: ArrayList<ArrayList<String>>?,
    var chapters_len: Int,
    var created: Double,
    var description: String?,
    var hits: Int,
    var image: String?,
    var imageURL: String?,
    var language: Int,
    var last_chapter_date: Long,
    var released: Long,
    var startsWith: String?,
    var title: String?,
    var title_kw: ArrayList<String>?
) {
    val categoriesAsString: String
        get() {
            return categories?.joinToString(", ") ?: ""
        }
}
