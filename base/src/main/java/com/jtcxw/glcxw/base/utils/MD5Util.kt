package com.jtcxw.glcxw.base.utils

import java.security.MessageDigest

class MD5Util {
    companion object {
        fun md5(input: String): String {
            val bytes = MessageDigest.getInstance("MD5").digest(input.toByteArray())
            return printHexBinary(bytes)
        }

        fun printHexBinary(data: ByteArray): String {
            val HEX_CHARS = "0123456789ABCDEF".toCharArray()
            val r = StringBuilder(data.size * 2)
            data.forEach { b ->
                val i = b.toInt()
                r.append(HEX_CHARS[i shr 4 and 0xF])
                r.append(HEX_CHARS[i and 0xF])
            }
            return r.toString()
        }
    }
}