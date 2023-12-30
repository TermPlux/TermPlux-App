package io.termplux.ui.view

import android.content.Context
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import io.termplux.ui.theme.TermPluxAppTheme

class UI private constructor() : UIWrapper {

    override fun content(context: Context): View {
        return withView(context) {
            TermPluxAppTheme {
                Scaffold { innerPadding ->
                    Surface(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

                    }

                }
            }
        }
    }

    private fun withView(
        context: Context,
        content: @Composable () -> Unit,
    ): ComposeView {
        return ComposeView(
            context = context
        ).apply {
            setContent(
                content = content
            )
        }
    }

    companion object {

        fun build(): UI {
            return UI()
        }
    }
}