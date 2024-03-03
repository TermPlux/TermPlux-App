package io.termplux.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.gaurav.avnc.ui.about.AboutActivity
import com.gaurav.avnc.util.AppPreferences
import com.wyq0918dev.flutter_mixed.MixedActivity
import com.wyq0918dev.viewcontainerview.ViewContainerManager
import com.wyq0918dev.viewcontainerview.ViewContainerView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.termplux.databinding.ContainerBinding
import io.termplux.R
import io.termplux.ui.theme.TermPluxTheme

class TermPluxActivity : MixedActivity(), ActivityWrapper {

    private lateinit var mBinding: ContainerBinding
    private lateinit var mNavController: NavController
    private lateinit var mBundle: Bundle

   // @Keep
    lateinit var prefs: AppPreferences

    private fun updateNightMode(theme: String) {
        val nightMode = when (theme) {
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            "dark" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        prefs = AppPreferences(this)
        prefs.ui.theme.observeForever { updateNightMode(it) }
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "avnc").setMethodCallHandler { call, result ->
            // 判断方法名
            when (call.method) {
                "launchUsingUri" -> {
                    com.gaurav.avnc.ui.vnc.startVncActivity(this, com.gaurav.avnc.vnc.VncUri(call.argument("vncUri")!!))
                    result.success(0)
                }
                "launchPrefsPage" -> {
                    startActivity(Intent(this, com.gaurav.avnc.ui.prefs.PrefsActivity::class.java))
                    result.success(0)
                }
                "launchAboutPage" -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                    result.success(0)
                }
                else -> {
                    // 不支持的方法名
                    result.notImplemented()
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启用边倒边
        enableEdgeToEdge()
        
        mBundle = Bundle()

        mBinding = ContainerBinding.inflate(layoutInflater)
        val mFragmentContainerView = mBinding.navHostFragmentContentMain
        val navHostFragment =
            (supportFragmentManager.findFragmentById(mFragmentContainerView.id) as NavHostFragment?)!!
        mNavController = navHostFragment.navController
        mNavController.setGraph(R.navigation.nav_graph, mBundle)



        setContent {
            TermPluxTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AndroidView(
                            factory = { context ->
                                ViewContainerView(context = context)
                            },
                            modifier = Modifier.fillMaxSize(),
                            update = { view ->
                                ViewContainerManager.build()
                                    .withContainerView(container = view)
                                    .childView(child = mFragmentContainerView)
                                    .apply()

                            }
                        )
                    }
                }
            }
        }

    }

    override fun getFragment(): Fragment = flutter
}