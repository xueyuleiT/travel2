package com.glcxw.lib.util

object PwdUtil {
    //数字
    val REG_NUMBER = ".*\\d+.*"
    //小写字母
    val REG_UPPERCASE = ".*[A-Z]+.*"
    //大写字母
    val REG_LOWERCASE = ".*[a-z]+.*"
    //特殊符号

    fun checkPasswordRule(password: String?): Boolean {
        //密码为空或者长度小于8位则返回false
        if (password == null || password.length < 8) return false
        var i = 0
        if (password.matches(REG_NUMBER.toRegex())) i++
        if (password.matches(REG_LOWERCASE.toRegex())) i++
        if (password.matches(REG_UPPERCASE.toRegex())) i++

        return i >= 3

    }
}