package com.jtcxw.glcxw.dialog

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BaseBottomSheetDialogFragment
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.TypeDialogAdapter
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.RefreshLoadMoreRecyclerView
import com.jtcxw.glcxw.listeners.BusDialogCallback
import com.jtcxw.glcxw.listeners.KVDialogCallback
import com.jtcxw.glcxw.localbean.KVBean

class KVTypeDialog : BaseBottomSheetDialogFragment() {
    override fun getLayoutId(): Int {
        return R.layout.dialog_type
    }
    var mDialogCallback: KVDialogCallback?= null

    fun setCallback(dialogCallback: KVDialogCallback):KVTypeDialog {
        mDialogCallback = dialogCallback
        return this
    }

    var mList = ArrayList<KVBean>()



    fun setData(list: ArrayList<KVBean>):KVTypeDialog {
        mList.clear()
        mList.addAll(list)
        return this
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recycler = view.findViewById<RefreshLoadMoreRecyclerView>(R.id.recycler_view)
        val adapter = TypeDialogAdapter(context!!,mList!!)
        adapter.setOnItemClickListener(object :
            BaseRecyclerAdapter.OnItemClickListener<KVBean>{
            override fun onItemClick(
                view: View?,
                model: KVBean?,
                position: Int
            ) {
                mList!!.forEach {
                    it.checked = false
                }
                model!!.checked = true
                if (mDialogCallback != null) {
                    mDialogCallback!!.onKVDialogCallback(position)
                }
                dismiss()
            }

            override fun onItemLongClick(
                view: View?,
                model: KVBean?,
                position: Int
            ) {
            }

        })

        recycler.adapter = adapter

    }
}