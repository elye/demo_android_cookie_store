package com.elyeproj.demoappwebviewcookie

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        // This is required to persist the cookie that was set by the webview
        webviewCookierHandler.persistCookie()

        readCookieFromCookieManager()
    }

    fun readCookie(view: View) {
        fetchNetwork.subscribe({ readCookieFromCookieManager() },{
            e -> txt_cookie_value.text = e.localizedMessage
        })
    }

    fun clearCookie(view: View) {
        webviewCookierHandler.clearCookie()
        readCookieFromCookieManager()
    }

    fun openWebview(view: View) {
        val intent = Intent(this, WebviewActivity::class.java)
        startActivity(intent)
    }

    private fun readCookieFromCookieManager() {
        val cookie = webviewCookierHandler.getCookie(URL, COOKIE_NAME)
        if (!cookie.isNullOrEmpty()) {
            txt_cookie_value.text = cookie
        } else {
            txt_cookie_value.setText(R.string.txt_cookie_nothing)
        }
    }

}
