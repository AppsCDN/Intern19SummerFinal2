package com.example.intern19summerfinal2.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intern19summerfinal2.R
import com.example.intern19summerfinal2.adapter.ZoomAdapter
import com.example.intern19summerfinal2.adapter.ZoomViewPager
import com.example.intern19summerfinal2.model.Page
import com.example.intern19summerfinal2.utils.makeFullScreen
import java.util.ArrayList

class ZoomActivity : AppCompatActivity() {
    private lateinit var zoomViewPager: ZoomViewPager
    private lateinit var pageList: MutableList<Page>

    companion object {

        private const val IMAGES_TAG = "images"
        private const val POSITION_TAG = "position"

        fun newIntent(context: Context, pageList: ArrayList<Page>, position: Int): Intent {
            val intent = Intent(context, ZoomActivity::class.java)
            intent.putParcelableArrayListExtra(IMAGES_TAG, pageList)
            intent.putExtra(POSITION_TAG, position)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        setContentView(R.layout.activity_zoom)
        initPager()
    }

    override fun onBackPressed() {
        returnResultOnExit()
        super.onBackPressed()
    }

    private fun returnResultOnExit() {
        val intent = Intent()
        intent.putExtra(POSITION_TAG, zoomViewPager.currentItem)
        setResult(Activity.RESULT_OK, intent)
    }

    private fun initPager() {
        pageList = intent?.extras?.getParcelableArrayList(IMAGES_TAG) ?: mutableListOf()
        val position = intent?.extras?.getInt(POSITION_TAG) ?: 0


        zoomViewPager = findViewById(R.id.zoomViewPager)
        val adapter = ZoomAdapter(pageList)
        zoomViewPager.adapter = adapter
        zoomViewPager.currentItem = position
    }
}
