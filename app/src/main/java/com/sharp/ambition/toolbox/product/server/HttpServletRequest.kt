package com.sharp.ambition.toolbox.product.server

/**
 *    author : fengqiao
 *    date   : 2021/11/29 19:31
 *    desc   :
 */
data class HttpServletRequest(var method: String = "", var requestURI: String = "", var headers: MutableMap<String, String> = mutableMapOf()) {

    fun getHeader(key: String): String? {
        return headers[key]
    }

}