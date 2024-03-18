package com.eltex.androidschool.util

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.yandex.mapkit.mapview.MapView


class DisabledMapView(context: Context?, attrs: AttributeSet?) :
    MapView(context, attrs) {
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }

            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}