package io.termplux.app.activity

import android.os.Bundle
import android.widget.FrameLayout
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.calculateDisplayFeatures
import io.termplux.app.adapter.PreferenceAdapter
import io.termplux.app.framework.termplux.ComposeActivity
import io.termplux.app.ui.layout.ActivityMain
import io.termplux.app.ui.theme.TermPluxTheme

class MainActivity : ComposeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    override fun Contents() {
        super.Contents()

        val windowSize: WindowSizeClass = calculateWindowSizeClass(activity = this@MainActivity)
        val displayFeatures: List<DisplayFeature> =
            calculateDisplayFeatures(activity = this@MainActivity)
        val preferenceAdapter = PreferenceAdapter(activity = this@MainActivity)

        val poem = listOf(
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

        TermPluxTheme {
            ActivityMain(
                windowSize = windowSize,
                displayFeatures = displayFeatures,
                rootLayout = FrameLayout(this@MainActivity),
                appsUpdate = {},
                topBarVisible = true,
                topBarUpdate = { toolbar ->
                    setSupportActionBar(toolbar)
                    supportActionBar?.subtitle = poem[(poem.indices).random()]
                },
                preferenceUpdate = {preference ->
                    preference.apply {
                        adapter = preferenceAdapter
                        offscreenPageLimit = preferenceAdapter.itemCount
                    }
                },
                androidVersion = "13",
                shizukuVersion = "13",
                current = {},
                toggle = {},
                taskbar = {}
            )
        }
    }
}