package com.elyeproj.apppersistentcookiestore

import android.content.Context
import com.elyeproj.basemodule.BasePresenter
import com.elyeproj.basemodule.BaseView
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.HttpUrl
import okhttp3.OkHttpClient


class MainPresenter(view: BaseView, context: Context) : BasePresenter(view) {

    private val cookieJar = MyPersistentCookieJar(context)
    private val _okHttpClient = OkHttpClient().newBuilder().cookieJar(cookieJar).build()

    override val okHttpClient: OkHttpClient
        get() =  _okHttpClient

    override fun onResume() {
        // This is required to persist the cookie that was set by the webview
        super.readFromCookieStore()
    }

    override fun clearCookie() {
        cookieJar.clear()
        super.clearCookie()
    }

    override fun readFromCookieStore(urlString: String, cookieName: String) {
        val cookie = cookieJar.getCookie(urlString, cookieName)
        super.showCookie(cookie)
    }
}
