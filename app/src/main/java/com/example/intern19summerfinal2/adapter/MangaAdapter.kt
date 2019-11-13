package com.example.intern19summerfinal2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.intern19summerfinal2.R
import com.example.intern19summerfinal2.model.Manga
import com.example.intern19summerfinal2.utils.AdapterOnItemClickListener
import com.example.intern19summerfinal2.utils.formatDate
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer

class MangaAdapter(private var listManga: MutableList<Manga>, private val listener: AdapterOnItemClickListener<Manga>) : RecyclerView.Adapter<MangaAdapter.ViewHolder>() {
    companion object{
        private const val IMAGE_URL = "https://cdn.mangaeden.com/mangasimg/"
    }

//    fun setData(manga: MutableList<Manga>) {
//        this.listManga = manga
//    }

    fun setCategories(manga: MutableList<Manga>) {
        listManga = mutableListOf()
        listManga.clear()
        listManga.addAll(manga)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_manga, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listManga.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listManga[position], listener)
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val tvMangaTitle: TextView = containerView.findViewById(R.id.tvMangaTitle)
        private val tvMangaRatingNumber: TextView = containerView.findViewById(R.id.tvMangaRatingNumber)
        private val tvMangaDate: TextView = containerView.findViewById(R.id.tvMangaDate)
        private val imgManga: ImageView = containerView.findViewById(R.id.imgManga)

        internal fun bind(mangaItem: Manga, listener: AdapterOnItemClickListener<Manga>) {
            val image = mangaItem.image
            image?.let {
                Picasso.with(itemView.context)
                    .load(IMAGE_URL + image)
                    .placeholder(R.drawable.img_loading)
                    .error(R.drawable.img_noimage)
                    .into(imgManga)
            }
            tvMangaTitle.text = mangaItem.title
            tvMangaRatingNumber.text = mangaItem.hits.toString()
            tvMangaDate.text = mangaItem.lastChapterDate.formatDate()

            itemView.setOnClickListener { listener.onItemClick(mangaItem) }
        }
    }

    fun setSearchOperation(filterList: MutableList<Manga>) {
        listManga = mutableListOf()
        listManga.clear()
        listManga.addAll(filterList)
        notifyDataSetChanged()
    }
}
