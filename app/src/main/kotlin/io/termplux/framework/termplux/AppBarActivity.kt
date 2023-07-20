package io.termplux.framework.termplux

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import io.termplux.R
import rikka.core.ktx.unsafeLazy

abstract class AppBarActivity : AppActivity() {

    private val rootView: ViewGroup by unsafeLazy {
        findViewById(R.id.root)
    }

    private val toolbarContainer: AppBarLayout by unsafeLazy {
        findViewById(R.id.toolbar_container)
    }

    private val toolbar: Toolbar by unsafeLazy {
        findViewById(R.id.toolbar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(getLayoutId())

        setSupportActionBar(toolbar)
    }

    @LayoutRes
    open fun getLayoutId(): Int {
        return R.layout.appbar_activity
    }

    override fun setContentView(layoutResID: Int) {
        layoutInflater.inflate(layoutResID, rootView, true)
        rootView.bringChildToFront(toolbarContainer)
    }

    override fun setContentView(view: View?) {
        setContentView(
            view = view,
            params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        rootView.addView(view, 0, params)
    }

    override fun onApplyTranslucentSystemBars() {
        super.onApplyTranslucentSystemBars()
        window?.statusBarColor = Color.TRANSPARENT
    }
}
//
//abstract class AppBarFragmentActivity : AppBarActivity() {
//
//    override fun getLayoutId(): Int {
//        return R.layout.appbar_fragment_activity
//    }
//}