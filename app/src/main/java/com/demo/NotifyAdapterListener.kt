package com.demo

interface NotifyAdapterListener {

    fun notifyAdapter()

    fun notifyShowPage(isCurrent: Boolean,position:Int)

}