package com.elyeproj.apppersistentwebsynccookie

import android.webkit.CookieManager
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.cache.CookieCache
import com.franmontiel.persistentcookiejar.persistence.CookiePersistor
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.util.*

open class MyPersistentCookieJar(private val cache: CookieCache, private val persistor: CookiePersistor) : ClearableCookieJar {

    private val cookieManager = CookieManager.getInstance()

    init {
        this.cache.addAll(persistor.loadAll())
    }

    @Synchronized override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cache.addAll(cookies)
        persistor.saveAll(filterPersistentCookies(cookies))
    }

    private fun filterPersistentCookies(cookies: List<Cookie>): List<Cookie> {
        return cookies.filter { it.persistent() }
    }

    @Synchronized override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return getCookies { it.matches(url) }
    }

    private fun getCookies(condition: (Cookie) -> Boolean): ArrayList<Cookie> {
        val cookiesToRemove = ArrayList<Cookie>()
        val validCookies = ArrayList<Cookie>()

        val it = cache.iterator()
        while (it.hasNext()) {
            val currentCookie = it.next()

            if (isCookieExpired(currentCookie)) {
                cookiesToRemove.add(currentCookie)
                it.remove()

            } else if (condition(currentCookie)) {
                validCookies.add(currentCookie)
            }
        }

        persistor.removeAll(cookiesToRemove)

        return validCookies
    }

    private fun isCookieExpired(cookie: Cookie): Boolean {
        return cookie.expiresAt() < System.currentTimeMillis()
    }

    @Synchronized override fun clearSession() {
        cache.clear()
        cache.addAll(persistor.loadAll())
    }

    @Synchronized override fun clear() {
        cache.clear()
        persistor.clear()
        cookieManager.removeAllCookies(null)
        cookieManager.flush()
    }


    fun getCookie(urlString: String, cookieName: String): String? {
        var cookieValue: String? = null
        val httpUrl = HttpUrl.parse(urlString) ?: return null
        val cookies = loadForRequest(httpUrl)

        cookies.toString().splitIntoArray(";")
                .filter { it.contains(cookieName) }
                .map { it.splitIntoArray("=") }
                .forEach { cookieValue = it[1] }

        return cookieValue

    }

    private fun String.splitIntoArray(delimiter: String) =
            this.split(delimiter).dropLastWhile { it.isEmpty() }.toTypedArray()


    @Synchronized fun syncAppCookiesToWebView() {
        val cookies = getCookies { true }

        for (cookie in cookies) {
            val url = getCookieUrl(cookie)
            val value = getCookieValue(cookie)
            cookieManager.setCookie(url, value)
        }
        cookieManager.flush()
    }

    private fun getCookieUrl(cookie: Cookie): String {
        return (if (cookie.secure()) "https" else "http") + "://" + cookie.domain() + cookie.path()
    }

    private fun getCookieValue(cookie: Cookie): String {
        val result = StringBuilder().append(cookie.toString())
        result.append("; Path=").append(cookie.path())
        result.append("; Domain=").append(cookie.domain())
        if (cookie.secure()) {
            result.append("; secure")
        }
        return result.toString()
    }

}