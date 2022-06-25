package com.elyeproj.basemodule

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseActivity : AppCompatActivity(), BaseView {

    abstract protected val presenter : BasePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    fun readCookie(view: View) {
        presenter.readCookie()
    }

    fun clearCookie(view: View) {
        presenter.clearCookie()
    }

    fun openWebview(view: View) {
        presenter.onOpenWebview()
        val intent = Intent(this, WebviewActivity::class.java)
        startActivity(intent)
    }

    override fun showCookie(cookie: String) {
        txt_cookie_value.text = cookie
    }

    override fun showNothing() {
        txt_cookie_value.setText(R.string.txt_cookie_nothing)
    }

    override fun setErrorMessage(localizedMessage: String) {
        txt_cookie_value.text = localizedMessage
    }
}