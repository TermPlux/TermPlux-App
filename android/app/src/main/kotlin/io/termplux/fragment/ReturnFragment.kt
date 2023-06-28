package io.termplux.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.android.FlutterView
import io.termplux.utils.FlutterViewReturn

class ReturnFragment : FlutterBoostFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = Space(context).apply {
        super.onCreateView(inflater, container, savedInstanceState)?.let { parent ->
            when (val activity = requireActivity()) {
                is FlutterViewReturn -> {
                    (activity as FlutterViewReturn).apply {
                        onFlutterViewReturned(
                            flutterView = findFlutterView(
                                view = parent
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

    private fun errorFindView(): Nothing {
        error("无法获取控件")
    }

    private fun errorViewType(): Nothing {
        error("未知的类型")
    }
}