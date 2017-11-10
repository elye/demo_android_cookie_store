package com.elyeproj.basemodule

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request

abstract class BasePresenter(val view: BaseView) {
    companion object {
        private const val URL = "http://192.168.1.11/experiments/cookies/set_simple.php"
        private const val COOKIE_NAME = "android_webview_communicate_cookie"
    }

    abstract protected val okHttpClient : OkHttpClient

    private val fetchNetwork = Completable.fromCallable {
        val request = Request.Builder().url(URL).build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()

        if (!response.isSuccessful) {
            throw IllegalStateException("Network fetch failure")
        }
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    open fun onResume() {}

    fun readCookie() {
        fetchNetwork.subscribe({ readFromCookieStore() },{
            e -> view.setErrorMessage(e.localizedMessage)
        })
    }

    open fun clearCookie() {
        readFromCookieStore()
    }

    open fun onOpenWebview() {
        // By default do nothing
    }

    abstract protected fun readFromCookieStore(url: String, cookieName: String)

    protected fun readFromCookieStore() {
        readFromCookieStore(URL, COOKIE_NAME)
    }

    protected fun showCookie(cookie: String?) {
        if (cookie != null && cookie.isNotEmpty()) {
            view.showCookie(cookie)
        } else {
            view.showNothing()
        }
    }


}