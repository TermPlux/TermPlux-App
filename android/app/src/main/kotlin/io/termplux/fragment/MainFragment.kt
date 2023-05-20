package io.termplux.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainFragment constructor(

): FlutterBoostFragment() {

    private lateinit var mContext: Context
    private lateinit var mFlutterView: View

    private lateinit var channel: MethodChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 获取父类的布局
        super.onCreateView(inflater, container, savedInstanceState)?.let {
            mFlutterView = it
        }
        // 返回新的布局
        return LinearLayoutCompat(mContext).apply {
            orientation = LinearLayoutCompat.VERTICAL
            addView(mFlutterView)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        val messenger = flutterEngine.dartExecutor.binaryMessenger
        channel = MethodChannel(messenger, "termplux_channel")
        channel.setMethodCallHandler { call, res ->
            when (call.method) {
                "pager" -> {
                    Toast.makeText(requireActivity(), "6", Toast.LENGTH_SHORT).show()
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

        fun newInstance(): MainFragment{
            return MainFragment()
        }
    }
}