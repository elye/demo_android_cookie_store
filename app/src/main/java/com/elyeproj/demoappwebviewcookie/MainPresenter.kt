package com.elyeproj.demoappwebviewcookie

import com.elyeproj.basemodule.BasePresenter
import com.elyeproj.basemodule.BaseView
import okhttp3.OkHttpClient

class MainPresenter(view: BaseView) : BasePresenter(view) {

    private val webviewCookierHandler = WebviewCookieHandler()
    private val _okHttpClient = OkHttpClient().newBuilder().cookieJar(webviewCookierHandler).build()

    override val okHttpClient: OkHttpClient
        get() =  _okHttpClient

    override fun onResume() {
        // This is required to persist the cookie that was set by the webview
        webviewCookierHandler.persistCookie()
        super.readFromCookieStore()
    }

    override fun clearCookie() {
        webviewCookierHandler.clearCookie()
        super.clearCookie()
    }

    override fun readFromCookieStore(url: String, cookieName: String) {
        val cookie = webviewCookierHandler.getCookie(url, cookieName)
        super.showCookie(cookie)
    }
}
