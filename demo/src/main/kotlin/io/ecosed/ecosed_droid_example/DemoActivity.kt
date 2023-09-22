/**
 * Copyright EcosedDroid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.ecosed.ecosed_droid_example

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.ecosed.ecosed_droid_example.ui.theme.EDExampleTheme
import io.ecosed.droid.app.EcosedActivity
import io.ecosed.droid.app.EcosedLauncher
import io.ecosed.droid.app.EdgeToEdge
import io.ecosed.droid.app.IEcosedActivity
import io.ecosed.droid.app.NavBarBackgroundColor

@EcosedLauncher(isLauncher = true)
@NavBarBackgroundColor(color = Color.TRANSPARENT)
@EdgeToEdge(edge = true)
class DemoActivity : ComponentActivity(), IEcosedActivity by EcosedActivity<DemoApplication, DemoActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 将EcosedDroid附加到此Activity
        attachEcosed(activity = this@DemoActivity)

        // 设置布局，EcosedLauncher标记为true则设置主页布局，false为整个Activity的布局
        setContentComposable {
            EDExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(name = "EcosedDroid")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 将EcosedDroid与此Activity分离
        detachEcosed()
    }

    @Composable
    private fun Greeting(name: String) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Hello $name!"
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun GreetingPreview() {
        EDExampleTheme {
            Greeting(name = "EcosedDroid")
        }
    }
}