package io.termplux.app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.android.FlutterActivityLaunchConfigs.BackgroundMode
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.android.TransparencyMode

class MainActivity : FlutterFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    }

//    override fun createFlutterFragment(): FlutterFragment {
//        return FlutterBoostFragment.CachedEngineFragmentBuilder()
//            .destroyEngineWithFragment(false)
//            .renderMode(renderMode)
//            .transparencyMode(
//                if (backgroundMode == BackgroundMode.opaque) {
//                    TransparencyMode.opaque
//                } else {
//                    TransparencyMode.transparent
//                }
//            )
//            .shouldAttachEngineToActivity(false)
//            .build()
//    }
}
