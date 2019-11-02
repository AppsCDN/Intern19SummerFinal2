package com.example.intern19summerfinal2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.intern19summerfinal2.R
import com.example.intern19summerfinal2.model.Manga
import com.example.intern19summerfinal2.utils.OnItemClickListener
import com.example.intern19summerfinal2.utils.formatDefaultFromSeconds
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlin.collections.ArrayList

class MangaAdapter(private val listener: OnItemClickListener<Manga>) : RecyclerView.Adapter<MangaAdapter.ViewHolder>() {
    private val mangaList: ArrayList<Manga> = ArrayList()

    companion object{
        private const val IMAGE_URL = "https://cdn.mangaeden.com/mangasimg/"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_manga, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mangaList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mangaList[position], listener)
    }


    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val tvMangaTitle: TextView = containerView.findViewById(R.id.tvMangaTitle)
        private val tvMangaRating: TextView = containerView.findViewById(R.id.tvMangaRating)
        private val tvMangaUpdate: TextView = containerView.findViewById(R.id.tvMangaUpdate)
        private val imgManga: ImageView = containerView.findViewById(R.id.imgManga)

        internal fun bind(mangaItem: Manga, listener: OnItemClickListener<Manga>) {
            val image = mangaItem.image
            image?.let {
                Picasso.with(itemView.context)
//                    .load(MangaApiHelper.buildUrl(image))
                    .load(IMAGE_URL + image)
                    .placeholder(R.drawable.img_loading)
                    .error(R.drawable.img_noimage)
                    .into(imgManga)
            }
            tvMangaTitle.text = mangaItem.title
            tvMangaRating.text = mangaItem.hits.toString()
            tvMangaUpdate.text = mangaItem.lastChapterDate.formatDefaultFromSeconds()

            itemView.setOnClickListener { listener.onItemClick(mangaItem) }
        }
    }
}
