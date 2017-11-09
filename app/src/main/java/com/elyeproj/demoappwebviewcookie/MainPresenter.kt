package com.elyeproj.demoappwebviewcookie

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request

class MainPresenter(val view: MainView) {
    companion object {
        private const val URL = "http://10.150.39.35/experiments/cookies/set_simple.php"
        private const val COOKIE_NAME = "android_webview_communicate_cookie"
    }

    private val webviewCookierHandler = WebviewCookieHandler()

    private val okHttpClient = OkHttpClient().newBuilder().cookieJar(webviewCookierHandler).build()

    private val fetchNetwork = Completable.fromCallable {
        val request = Request.Builder().url(URL).build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()

        if (!response.isSuccessful) {
            throw IllegalStateException("Network fetch failure")
        }
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    fun onResume() {
        // This is required to persist the cookie that was set by the webview
        webviewCookierHandler.persistCookie()
        readCookieFromCookieManager()
    }

    fun readCookie() {
        fetchNetwork.subscribe({ readCookieFromCookieManager() },{
            e -> view.setErrorMessage(e.localizedMessage)
        })
    }

    fun clearCookie() {
        webviewCookierHandler.clearCookie()
        readCookieFromCookieManager()
    }

    private fun readCookieFromCookieManager() {
        val cookie = webviewCookierHandler.getCookie(URL, COOKIE_NAME)
        if (cookie != null && cookie.isNotEmpty()) {
            view.showCookie(cookie)
        } else {
            view.showNothing()
        }
    }
}
