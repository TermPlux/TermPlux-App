package io.termplux.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.viewpager2.widget.ViewPager2
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.termplux.adapter.ViewPager2Adapter

class MainFragment constructor(

) : FlutterBoostFragment() {

    private lateinit var mContext: Context
    private lateinit var mFlutterView: View
    private lateinit var mComposeView: ComposeView
    private lateinit var mViewPager2: ViewPager2

    private lateinit var channel: MethodChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireActivity()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 获取父类的布局
        super.onCreateView(inflater, container, savedInstanceState)?.let {
            mFlutterView = it
        }
        // 初始化ComposeView
        mComposeView = ComposeView(requireActivity()).apply {
            setContent {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        mViewPager2.currentItem = mViewPager2.currentItem - 1
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            },
                            title = {
                                Text(text = "Demo")
                            }
                        )
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ){
                        Text(text = "test")
                    }
                }
            }
        }
        // 初始化ViewPager
        mViewPager2 = ViewPager2(mContext).apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
        // 返回新的布局
        return mViewPager2
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainAdapter = ViewPager2Adapter(
            flutterView = mFlutterView,
            composeView = mComposeView
        )
        mViewPager2.apply {
            adapter = mainAdapter
            offscreenPageLimit = mainAdapter.itemCount
        }
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        val messenger = flutterEngine.dartExecutor.binaryMessenger
        channel = MethodChannel(messenger, "termplux_channel")
        channel.setMethodCallHandler { call, res ->
            when (call.method) {
                "pager" -> {
                    mViewPager2.currentItem = mViewPager2.currentItem + 1
                    res.success("success")
                }

                else -> {
                    res.error("error", "error_message", null)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    companion object {

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}