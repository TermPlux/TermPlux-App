package io.termplux.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.android.FlutterView
import io.termplux.utils.FlutterViewReturn

class FlutterFragment : FlutterBoostFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FrameLayout(context).apply {
        addView(
            ProgressBar(context),
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        )
    }.also {
        next(
            parent = super.onCreateView(
                inflater,
                container,
                savedInstanceState
            )
        )
    }

    private fun next(parent: View?) {
        when (val activity = requireActivity()) {
            is FlutterViewReturn -> {
                (activity as FlutterViewReturn).apply {
                    parent?.let { view ->
                        returnFlutterView(
                            flutterView = findFlutterView(
                                view = view
                            )
                        )
                    }
                }
            }
        }
    }

    private fun findFlutterView(view: View): FlutterView? {
        when (view) {
            is FlutterView -> return view
            is ViewGroup -> for (i in 0 until view.childCount) {
                return findFlutterView(
                    view = view.getChildAt(i)
                ) ?: errorFindView()
            }

            else -> errorViewType()
        }
        return null
    }

    private fun errorViewType(): Nothing {
        error("未知的类型")
    }

    private fun errorFindView(): Nothing {
        error("无法获取控件")
    }
}