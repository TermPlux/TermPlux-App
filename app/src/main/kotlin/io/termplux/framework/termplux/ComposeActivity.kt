package io.termplux.framework.termplux

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView

open class ComposeActivity : AppBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ComposeView(context = this@ComposeActivity).apply {
                setContent {
                    Contents()
                }
            }
        )
    }

    @Composable
    open fun Contents(){

    }
}