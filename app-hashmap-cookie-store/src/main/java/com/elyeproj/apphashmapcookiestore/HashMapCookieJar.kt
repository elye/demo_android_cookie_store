package com.elyeproj.apphashmapcookiestore

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class HashMapCookieJar : CookieJar {

    private val cookieStore = mutableMapOf<String, String>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore.put(url.toString(), cookies.toString())
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val urlString = url.toString()
        val cookiesString = cookieStore[urlString]

        if (cookiesString != null && cookiesString.isNotEmpty()) {
            //We can split on the ';' char as the cookie manager only returns cookies
            //that match the url and haven't expired, so the cookie attributes aren't included
            val cookieHeaders = cookiesString.splitIntoArray(";")

            return cookieHeaders.mapNotNull { Cookie.parse(url, it) }
        }
        return emptyList()
    }

    fun clearCookie() {
        cookieStore.clear()
    }

    fun getCookie(urlString: String, cookieName: String): String? {
        var cookieValue: String? = null
        val cookiesString = cookieStore[urlString]
        if (cookiesString != null && cookiesString.isNotEmpty()) {
            cookiesString.splitIntoArray(";")
                    .filter { it.contains(cookieName) }
                    .map { it.splitIntoArray("=") }
                    .forEach { cookieValue = it[1] }
        }
        return cookieValue
    }

    private fun String.splitIntoArray(delimiter: String) =
            this.split(delimiter).dropLastWhile { it.isEmpty() }.toTypedArray()

}
