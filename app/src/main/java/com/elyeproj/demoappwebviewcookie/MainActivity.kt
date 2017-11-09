package com.elyeproj.demoappwebviewcookie

class MainActivity : BaseActivity() {

    private val _presenter = MainPresenter(this)
    override val presenter: BasePresenter
        get() = _presenter
}
