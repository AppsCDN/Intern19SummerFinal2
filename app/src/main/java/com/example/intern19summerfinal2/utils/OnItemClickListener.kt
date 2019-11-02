package com.example.intern19summerfinal2.utils

import com.example.intern19summerfinal2.model.Manga

interface OnItemClickListener<T> {
    fun onItemClick(mangaItem: Manga)
}