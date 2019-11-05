package com.demo

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_card_layout.view.*


class MyCardAdapter(
    var context: Context,
    var list: MutableList<String>,
    var mDragListener: StartDragListener,
    var mNotifyAdapterListener: NotifyAdapterListener
) :
    RecyclerView.Adapter<MyCardAdapter.ViewHolder>(), ItemTouchMoveListener {

    //屏幕宽度
    private val screenWidth = UiUtils.getScreenWidth(context)

    override fun onItemRemove(position: Int) {

        /**
         * 说明：
         *
         * 没有直接使用 notifyDataSetChanged，而是 notifyItemRemoved 和 notifyItemChanged
         * 是为了使删除item后，后面的补上来的动画，显的"好看、流程"，没有"闪动"
         *
         * 但是，还是必须要有 notifyDataSetChanged，否则，就是只刷新了删除的那个位置，而不是全部刷新，
         * 会造成，后面的数据定位错误。
         *
         * 如：
         * 有数据 0、1、2、3、4、5。
         * 点击2，toast 弹出 "2"
         * 上滑删除 2，后，卡片变成了 0、1、3、4、5
         * 点击3，toast 弹出 "3"
         *
         * 所以，notifyDataSetChanged 做整体刷新，是有必要的。
         */

        if (position == list.size - 1) {
            list.removeAt(position);
            notifyItemRemoved(position);
            notifyItemChanged(if (position - 1 < 0) 0 else position - 1)
        } else {
            list.removeAt(position);
            notifyItemRemoved(position);
            if (position == 0) {
                notifyItemChanged(position)
            }
        }

        mNotifyAdapterListener.notifyAdapter()

    }

    private val layoutInflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var itemView = layoutInflater.inflate(R.layout.item_card_layout, parent, false)

        val viewHolder: ViewHolder = ViewHolder(itemView)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindData(list[position], position)

        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams

        layoutParams.width = (screenWidth * 0.8f).toInt()

        var marginStartOrEnd = (screenWidth * 0.1f).toInt()

        var marginItem = (screenWidth * 0.03f).toInt()

        layoutParams.leftMargin = if (position == 0) marginStartOrEnd else marginItem
        layoutParams.rightMargin =
            if (position == list.size - 1) marginStartOrEnd else marginItem
        holder.itemView.layoutParams = layoutParams

        holder.itemView.item_ll.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                if (event?.getAction() == MotionEvent.ACTION_DOWN) {
                    mDragListener.onStartDrag(holder);
                }
                return false;
            }
        })

    }

    fun addAll(list: MutableList<String>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindData(str: String, position: Int) {

            itemView.my_tv.text = str

            itemView.item_ll.setOnClickListener {

                mNotifyAdapterListener.notifyShowPage(true,position)

                Toast.makeText(context, "点击事件 adapterPosition is $adapterPosition", Toast.LENGTH_SHORT).show()
            }

        }
    }

}