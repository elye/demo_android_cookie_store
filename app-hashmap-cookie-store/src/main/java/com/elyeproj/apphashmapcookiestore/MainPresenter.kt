package com.elyeproj.apphashmapcookiestore

import com.elyeproj.basemodule.BasePresenter
import com.elyeproj.basemodule.BaseView
import okhttp3.OkHttpClient


class MainPresenter(view: BaseView) : BasePresenter(view) {

    private val cookieJar = HashMapCookieJar()
    private val _okHttpClient = OkHttpClient().newBuilder().cookieJar(cookieJar).build()

    override val okHttpClient: OkHttpClient
        get() =  _okHttpClient

    override fun onResume() {
        // This is required to persist the cookie that was set by the webview
        super.readFromCookieStore()
    }

    override fun clearCookie() {
        cookieJar.clearCookie()
        super.clearCookie()
    }

    override fun readFromCookieStore(url: String, cookieName: String) {
        val cookie = cookieJar.getCookie(url, cookieName)
        super.showCookie(cookie)
    }
}
