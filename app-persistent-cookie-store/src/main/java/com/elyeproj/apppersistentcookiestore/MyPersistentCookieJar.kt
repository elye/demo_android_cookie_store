package com.elyeproj.apppersistentcookiestore

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.HttpUrl

class MyPersistentCookieJar(context: Context) : PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context)) {
    fun getCookie(urlString: String, cookieName: String): String? {
        var cookieValue: String? = null
        val cookies = loadForRequest(HttpUrl.parse(urlString))

        cookies.toString().splitIntoArray(";")
                .filter { it.contains(cookieName) }
                .map { it.splitIntoArray("=") }
                .forEach { cookieValue = it[1] }

        return cookieValue

    }

    private fun String.splitIntoArray(delimiter: String) =
            this.split(delimiter).dropLastWhile { it.isEmpty() }.toTypedArray()

}