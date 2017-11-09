package com.elyeproj.demoappwebviewcookie

import android.webkit.CookieManager
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class WebviewCookieHandler : CookieJar {
    private val webviewCookieManager = CookieManager.getInstance()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val urlString = url.toString()

        for (cookie in cookies) {
            webviewCookieManager.setCookie(urlString, cookie.toString())
        }

        persistCookie()
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val urlString = url.toString()
        val cookiesString = webviewCookieManager.getCookie(urlString)

        if (!cookiesString.isNullOrEmpty()) {
            //We can split on the ';' char as the cookie manager only returns cookies
            //that match the url and haven't expired, so the cookie attributes aren't included
            val cookieHeaders = cookiesString.splitIntoArray(";")

            return cookieHeaders.mapNotNull { Cookie.parse(url, it) }
        }
        return emptyList()
    }

    fun clearCookie() {
        webviewCookieManager.removeAllCookies(null)
        persistCookie()
    }

    fun getCookie(urlString: String, cookieName: String): String? {
        var cookieValue: String? = null
        val cookiesString = webviewCookieManager.getCookie(urlString)
        if (!cookiesString.isNullOrEmpty()) {
            cookiesString.splitIntoArray(";")
                    .filter { it.contains(cookieName) }
                    .map { it.splitIntoArray("=") }
                    .forEach { cookieValue = it[1] }
        }
        return cookieValue
    }

    fun persistCookie() {
        webviewCookieManager.flush()
    }

    private fun String.splitIntoArray(delimiter: String) =
            this.split(delimiter).dropLastWhile { it.isEmpty() }.toTypedArray()

}
