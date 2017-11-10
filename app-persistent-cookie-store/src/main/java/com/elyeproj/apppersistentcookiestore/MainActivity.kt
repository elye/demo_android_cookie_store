package com.elyeproj.apppersistentcookiestore

import android.os.Bundle
import com.elyeproj.basemodule.BaseActivity
import com.elyeproj.basemodule.BasePresenter

class MainActivity : BaseActivity() {

    private lateinit var _presenter : MainPresenter
    override val presenter: BasePresenter
        get() = _presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _presenter = MainPresenter(this, this)
    }
}
