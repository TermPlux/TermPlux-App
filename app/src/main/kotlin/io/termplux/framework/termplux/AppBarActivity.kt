package io.termplux.framework.termplux

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.MaterialShapeDrawable
import io.termplux.R
import io.termplux.databinding.ContainerBinding
import rikka.core.ktx.unsafeLazy

abstract class AppBarActivity : AppActivity() {

    protected lateinit var mAppBar: AppBarLayout
    protected lateinit var mContainer: FragmentContainerView

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var mFragmentContainerView: FragmentContainerView
    private lateinit var mActionBar: ActionBar

    private lateinit var binding: ContainerBinding

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
        // 绑定布局
        binding = ContainerBinding.inflate(layoutInflater)
        // 获取Fragment容器控件
        mFragmentContainerView = binding.navHostFragmentContentMain
        // 绑定导航主机
        val navHostFragment = supportFragmentManager.findFragmentById(
            mFragmentContainerView.id
        ) as NavHostFragment
        // 导航控制器
        navController = navHostFragment.navController
        // 应用栏配置
        appBarConfiguration = AppBarConfiguration(
            navGraph = navController.graph
        )
        // 设置操作栏
        setSupportActionBar(toolbar)
        // 将操作栏与导航控制器绑定
        setupActionBarWithNavController(
            navController = navController,
            configuration = appBarConfiguration
        )
        // 获取操作栏
        supportActionBar?.let {
            mActionBar = it
        }
        mAppBar = toolbarContainer
        mContainer = mFragmentContainerView
        // 随机抽取诗句作为子标题
        mActionBar.subtitle = poem[(poem.indices).random()]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}