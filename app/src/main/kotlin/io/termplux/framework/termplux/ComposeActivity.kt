package io.termplux.framework.termplux

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable

open class ComposeActivity : AppBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Contents()
        }
    }

    @Composable
    open fun Contents() = Unit
}