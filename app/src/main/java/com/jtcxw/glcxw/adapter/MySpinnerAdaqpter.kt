package com.jtcxw.glcxw.adapter

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.jtcxw.glcxw.R

class MySpinnerAdaqpter:SpinnerAdapter {
    var mList:List<String> ?= null
    var mContext:Context ?= null
    constructor(context: Context,list: List<String>) {
        mList = list
        mContext = context
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
       val textView = (LayoutInflater.from(mContext!!).inflate(R.layout.item_spinner_phone,null) as TextView)
        textView.text= mList!![p0]
        return textView
    }

    override fun registerDataSetObserver(p0: DataSetObserver?) {
    }

    override fun getItemViewType(p0: Int): Int {
        return 1
    }

    override fun getItem(p0: Int): String {
      return mList!![p0]
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(p0: Int): Long {
       return p0.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getDropDownView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val textView = (LayoutInflater.from(mContext!!).inflate(R.layout.item_spinner_phone,null) as TextView)
        textView.text= mList!![p0]
        return textView
    }

    override fun unregisterDataSetObserver(p0: DataSetObserver?) {

    }

    override fun getCount(): Int {
        return mList!!.size
    }
}