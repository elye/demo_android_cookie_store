package com.elyeproj.demoappwebviewcookie

interface MainView {
    fun showCookie(cookie: String)
    fun showNothing()
    fun setErrorMessage(localizedMessage: String)
}