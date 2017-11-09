package com.elyeproj.demoappwebviewcookie

interface BaseView {
    fun showCookie(cookie: String)
    fun showNothing()
    fun setErrorMessage(localizedMessage: String)
}