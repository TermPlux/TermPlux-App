package io.termplux.app

import com.kongzue.baseframework.util.JumpParameter
import io.termplux.app.ui.ActivityMain
import io.termplux.basic.activity.TermPluxActivity

class MainActivity : TermPluxActivity() {


    override fun onCreated(parameter: JumpParameter?) {
        setContents { navController, content, event, message, current ->
            ActivityMain(
                navController = navController,
                topBar = { modifier ->
                    content(
                        content = topBar,
                        modifier = modifier
                    )
                },
                pager = { modifier ->
                    content(
                        content = pager,
                        modifier = modifier
                    )
                },
                tabBar = { modifier ->
                    content(
                        content = tabBar,
                        modifier = modifier
                    )
                },
                optionsMenu = {
                    event(options)
                },
                androidVersion = message(androidVersion),
                shizukuVersion = message(shizukuVersion),
                current = current,
                toggle = {
                    event(toggle)
                }
            )
        }
    }
}