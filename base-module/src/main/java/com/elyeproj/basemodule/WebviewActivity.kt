package com.elyeproj.basemodule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_webview.*

class WebviewActivity : AppCompatActivity() {

    companion object {
        private const val URL = "http://10.0.2.2/reset_simple.php"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState != null) {
            webview_main.restoreState(savedInstanceState)
        } else {
            webview_main.loadUrl(URL)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webview_main.saveState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
