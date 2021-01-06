package com.glcxw.lib.util

import android.text.TextUtils
import java.util.regex.Pattern

class RuleUtil {
    companion object {


        //数字
        private const val REG_NUMBER = ".*\\d+.*"
        //小写字母
        private const val REG_UPPERCASE = ".*[A-Z]+.*"
        //大写字母
        private const val REG_LOWERCASE = ".*[a-z]+.*"
        //特殊符号

        fun checkPasswordRule(password: String?): Boolean {
            //密码为空或者长度小于8位则返回false
            if (password == null || password.length < 8) return false
            var i = 0
            if (password.matches(REG_NUMBER.toRegex())) i++
            if (password.matches(REG_LOWERCASE.toRegex())) i++
            if (password.matches(REG_UPPERCASE.toRegex())) i++
            if (!isSpecialChar(password)) i++
            return i >= 4

        }

        fun isPhone(phone: String): Boolean {
            if (TextUtils.isEmpty(phone)){
                return false
            }

            return if (phone.length != 11) {
                false
            } else if (phone[1] == '0' || phone[1] == '1' || phone[1] == '2') {
                false
            } else {
               phone.startsWith("1")
            }
        }

         fun mobileEncrypt(mobile:String):String {
            if (TextUtils.isEmpty(mobile) || (mobile.length != 11)) {
                return mobile
            }
            return mobile.replace("(\\d{3})\\d{4}(\\d{4})", "$1****$2")
        }

        fun isSpecialChar(str: String): Boolean {
            val regEx =
                "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t"
            val p = Pattern.compile(regEx)
            val m = p.matcher(str)
            return m.find()
        }

    }
}