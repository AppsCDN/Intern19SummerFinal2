package com.example.intern19summerfinal2.utils

import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

//use before setContentView()
fun AppCompatActivity.makeFullScreen() {
    supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
}
