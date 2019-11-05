package com.demo

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class MyItemTouchHelperCallback : ItemTouchHelper.Callback {


    private var moveListener: ItemTouchMoveListener? = null


    constructor(moveListener: ItemTouchMoveListener) {
        this.moveListener = moveListener
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {

        /**
         * 要监听的swipe侧滑方向是哪个方向
         *
         * 类型有 UP、DOWN、LEFT、RIGHT 等
         */
        var swipeFlags: Int = ItemTouchHelper.UP
        var flags: Int = makeMovementFlags(0, swipeFlags);
        return flags;

    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        moveListener?.onItemRemove(viewHolder.getAdapterPosition());
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
    }

    /**
     * 针对swipe状态，swipe滑动的位置超过了百分之多少就消失
     */
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.2f
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}