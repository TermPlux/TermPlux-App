package io.termplux.app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.wyq0918dev.flutter_mixed.MixedActivity

class TermPluxActivity : MixedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(flutter)
    }
}