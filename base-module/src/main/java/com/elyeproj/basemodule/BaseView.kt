package com.elyeproj.basemodule

interface BaseView {
    fun showCookie(cookie: String)
    fun showNothing()
    fun setErrorMessage(localizedMessage: String)
}