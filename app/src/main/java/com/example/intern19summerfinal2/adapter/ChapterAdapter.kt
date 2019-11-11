package com.example.intern19summerfinal2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.intern19summerfinal2.R
import com.example.intern19summerfinal2.model.Chapter
import com.example.intern19summerfinal2.utils.formatDate
import kotlinx.android.extensions.LayoutContainer

class ChapterAdapter(private var listChapter: MutableList<Chapter>) : RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_chapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listChapter.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listChapter[position])
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val tvMangaChapter: TextView = containerView.findViewById(R.id.tvMangaChapter)
        private val tvMangaChapterTitle: TextView = containerView.findViewById(R.id.tvMangaChapterTitle)
        private val tvMangaChapterDateSub: TextView = containerView.findViewById(R.id.tvMangaChapterDateSub)

        internal fun bind(chapterItem: Chapter) {
            tvMangaChapter.text = chapterItem.number.toString()
            tvMangaChapterTitle.text = chapterItem.title
            tvMangaChapterDateSub.text = chapterItem.date?.formatDate()

//            itemView.setOnClickListener { listener.onItemClick(chapterItem) }
        }
    }
}
