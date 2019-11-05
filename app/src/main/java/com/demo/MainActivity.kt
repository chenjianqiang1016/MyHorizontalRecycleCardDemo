package com.demo

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

/**
 * 竖直滑动（删除）事件流程
 *
 * MyCardAdapter 中的 onTouch -> MainActivity 中的 onStartDrag -> ItemTouchHelper
 *
 * ItemTouchHelper 持有 MyItemTouchHelperCallback （即：ItemTouchHelper.Callback），
 *
 * MyItemTouchHelperCallback 持有 ItemTouchMoveListener （即：MyCardAdapter）
 *
 * ItemTouchHelper 的操作，会回调到 MyItemTouchHelperCallback 中的 onSwiped，
 *
 * 然后，调用 ItemTouchMoveListener 的 onItemRemove 方法，即：调用了 MyCardAdapter 的 onItemRemove 方法。
 *
 * 最后，就是各种刷新操作了
 */
class MainActivity : AppCompatActivity(), StartDragListener, NotifyAdapterListener {

    private var myAdapter: MyCardAdapter? = null

    private var mLayoutManager: SmoothScrollLayoutManager by Delegates.notNull()

    private val HandlerCode: Int = 20191105

    private var mHandler = object : Handler() {

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            when (msg?.what) {

                HandlerCode -> {
                    Log.e("Handler ", "刷新")
                    myAdapter?.notifyDataSetChanged()
                }

            }
        }
    }

    override fun notifyAdapter() {
        mHandler.sendEmptyMessageDelayed(HandlerCode, 500)
    }

    override fun notifyShowPage(isCurrent: Boolean, position: Int) {

        try {
            Log.e("position ", "$position")

            Log.e("first ", "${mLayoutManager.findFirstVisibleItemPosition()}")
            Log.e("complete ", "${mLayoutManager.findFirstCompletelyVisibleItemPosition()}")
            Log.e("last ", "${mLayoutManager.findLastVisibleItemPosition()}")

            if (mLayoutManager.findFirstCompletelyVisibleItemPosition() != position) {
                Log.e("notifyShowPage ", "符合条件，允许切换")
                myRecycleView?.smoothScrollToPosition(position)
            }else{
                Log.e("notifyShowPage ", "点击是当前卡片，不需要切换")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private var itemTouchHelper: ItemTouchHelper? = null

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper?.startDrag(viewHolder);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        //设置方向
        mLayoutManager = SmoothScrollLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        myRecycleView.layoutManager = mLayoutManager

        //设置 adapter
        myAdapter = MyCardAdapter(this, mutableListOf(), this, this)
        myRecycleView.adapter = myAdapter

        //控制一次只滑一页
        PagerSnapHelper().attachToRecyclerView(myRecycleView)

        //填充数据
        var dataList: MutableList<String> = mutableListOf()

        for (i in 0..5) {
            dataList.add("第 $i 个")
        }

        myAdapter?.addAll(dataList)

        myRecycleView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

        })


        var callback: ItemTouchHelper.Callback = MyItemTouchHelperCallback(myAdapter!!)
        itemTouchHelper = ItemTouchHelper(callback);
        itemTouchHelper?.attachToRecyclerView(myRecycleView);

    }
}
