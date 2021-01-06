package com.jtcxw.glcxw.views

import com.jtcxw.glcxw.base.respmodels.MemberAccountHistoryBean

interface AccountRecordView {
    fun onGetMemberAccountHistory(memberAccountHistoryBean: MemberAccountHistoryBean)
}