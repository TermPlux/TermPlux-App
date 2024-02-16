package io.termplux.app

import android.os.Bundle
import androidx.activity.compose.setContent

import androidx.activity.enableEdgeToEdge
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
import com.wyq0918dev.flutter_mixed.MixedActivity
import com.wyq0918dev.viewcontainerview.ViewContainerManager
import com.wyq0918dev.viewcontainerview.ViewContainerView
import io.flutter.embedding.android.FlutterActivity
import io.termplux.databinding.ContainerBinding
import io.termplux.R
import io.termplux.ui.theme.TermPluxTheme

class TermPluxActivity : FlutterActivity() {

//    private lateinit var mBinding: ContainerBinding
//    private lateinit var mNavController: NavController
//    private lateinit var mBundle: Bundle
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // 启用边倒边
//        enableEdgeToEdge()
//
//        mBinding = ContainerBinding.inflate(layoutInflater)
//        val mFragmentContainerView = mBinding.navHostFragmentContentMain
//        val navHostFragment =
//            (supportFragmentManager.findFragmentById(mFragmentContainerView.id) as NavHostFragment?)!!
//        mNavController = navHostFragment.navController
//        mNavController.setGraph(R.navigation.nav_graph, mBundle)
//
//
//
//        setContent {
//            TermPluxTheme {
//                Scaffold(
//                    modifier = Modifier.fillMaxSize()
//                ) { innerPadding ->
//                    Surface(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(paddingValues = innerPadding),
//                        color = MaterialTheme.colorScheme.background
//                    ) {
//                        AndroidView(
//                            factory = { context ->
//                                ViewContainerView(context = context)
//                            },
//                            modifier = Modifier.fillMaxSize(),
//                            update = { view ->
//                                ViewContainerManager.build()
//                                    .withContainerView(container = view)
//                                    .childView(child = mFragmentContainerView)
//                                    .apply()
//
//                            }
//                        )
//                    }
//                }
//            }
//        }
//
//    }
//
//    override fun getFragment(): Fragment = flutter
}