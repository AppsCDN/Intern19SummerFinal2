package com.example.intern19summerfinal2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.intern19summerfinal2.R
import com.example.intern19summerfinal2.model.Page
import com.github.chrisbanes.photoview.PhotoView
import com.bumptech.glide.request.target.Target

class ZoomAdapter(private var listPage: MutableList<Page>) : PagerAdapter() {
    companion object {
        private const val IMAGE_URL = "https://cdn.mangaeden.com/mangasimg/"
        private const val HEIGHT = 2000
        private const val WIDTH = 500
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun getCount(): Int {
        return listPage.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = layoutInflater.inflate(R.layout.item_page_zoom, container, false)
        val photoView = itemView.findViewById<PhotoView>(R.id.photoView)

//        for very long images
        var width = Target.SIZE_ORIGINAL
        val pagePosition = listPage[position]
        if (pagePosition.height > HEIGHT) {
            width = WIDTH
        }

        Glide.with(container.context)
            .load(IMAGE_URL + pagePosition.url)
            .error(R.drawable.img_noimage)
            .override(width, Target.SIZE_ORIGINAL)
            .into(photoView)

        container.addView(itemView)
        return itemView
    }
}
