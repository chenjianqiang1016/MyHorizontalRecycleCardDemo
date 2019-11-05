package com.demo

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class SmoothScrollLayoutManager : LinearLayoutManager {


    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    ) {

    }


    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {

        var smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(recyclerView?.getContext()) {

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {

                return super.calculateSpeedPerPixel(displayMetrics)

//                try {
//                    if (displayMetrics != null) {
//                        //控制速度，150f 所在位置，数值越小，速度越快
//                        return 150f / displayMetrics?.densityDpi
//                    } else {
//                        return super.calculateSpeedPerPixel(displayMetrics)
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    return super.calculateSpeedPerPixel(displayMetrics)
//                }

            }

        }

        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);


    }


}