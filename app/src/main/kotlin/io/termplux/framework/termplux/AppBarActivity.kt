package io.termplux.framework.termplux

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.MaterialShapeDrawable
import rikka.core.ktx.unsafeLazy

abstract class AppBarActivity : AppActivity() {

    open lateinit var mAppBar: AppBarLayout

    private lateinit var mActionBar: ActionBar

    private val poem = listOf(
        "不向焦虑与抑郁投降，这个世界终会有我们存在的地方。",
        "把喜欢的一切留在身边，这便是努力的意义。",
        "治愈、温暖，这就是我们最终幸福的结局。",
        "我有一个梦，也许有一天，灿烂的阳光能照进黑暗森林。",
        "如果必须要失去，那么不如一开始就不曾拥有。",
        "我们的终点就是与幸福同在。",
        "孤独的人不会伤害别人，只会不断地伤害自己罢了。",
        "如果你能记住我的名字，如果你们都能记住我的名字，也许我或者“我们”，终有一天能自由地生存着。",
        "对于所有生命来说，不会死亡的绝望，是最可怕的审判。",
        "我不曾活着，又何必害怕死亡。"
    )

    private val toolbarContainer: AppBarLayout by unsafeLazy {
        AppBarLayout(this@AppBarActivity).apply {
            setBackgroundColor(Color.TRANSPARENT)
            isLiftOnScroll = true
            statusBarForeground = MaterialShapeDrawable.createWithElevationOverlay(
                this@AppBarActivity
            )
            addView(
                toolbar,
                AppBarLayout.LayoutParams(
                    AppBarLayout.LayoutParams.MATCH_PARENT,
                    AppBarLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }
    }

    private val toolbar: Toolbar by unsafeLazy {
        MaterialToolbar(this@AppBarActivity).apply {
            setBackgroundColor(Color.TRANSPARENT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置操作栏
        setSupportActionBar(toolbar)
        // 获取操作栏
        supportActionBar?.let {
            mActionBar = it
        }
        mAppBar = toolbarContainer
        // 随机抽取诗句作为子标题
        mActionBar.subtitle = poem[(poem.indices).random()]
    }






}