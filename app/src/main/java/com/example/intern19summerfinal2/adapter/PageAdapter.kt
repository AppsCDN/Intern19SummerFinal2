package com.example.intern19summerfinal2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.intern19summerfinal2.R
import com.example.intern19summerfinal2.model.Page
import kotlinx.android.extensions.LayoutContainer

open class PageAdapter(private val listPage: MutableList<Page>, private val listener: OnItemClickListener) : RecyclerView.Adapter<PageAdapter.ViewHolder>() {
    companion object {
        private const val IMAGE_URL = "https://cdn.mangaeden.com/mangasimg/"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_page, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listPage.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPage[position], listener)
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val imgPage: ImageView = containerView.findViewById(R.id.imgPage)

        fun bind(pageItem: Page, listener: OnItemClickListener) {
            val preload = 0.25f
            val url = pageItem.url
            url?.let {
                Glide.with(itemView.context)
                    .load(IMAGE_URL + url)
                    .thumbnail(preload)
                    .error(R.drawable.img_noimage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgPage)
            }
            imgPage.setOnClickListener { listener.onItemClick(pageItem, adapterPosition) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(page: Page, position: Int)
    }
}
