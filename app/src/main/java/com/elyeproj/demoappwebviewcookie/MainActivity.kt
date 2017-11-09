package com.elyeproj.demoappwebviewcookie

import com.elyeproj.basemodule.BaseActivity
import com.elyeproj.basemodule.BasePresenter

class MainActivity : BaseActivity() {

    private val _presenter = MainPresenter(this)
    override val presenter: BasePresenter
        get() = _presenter
}
