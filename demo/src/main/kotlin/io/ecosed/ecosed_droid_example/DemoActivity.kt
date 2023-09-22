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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        attachEcosed(
            activity = this@DemoActivity,
            lifecycle = lifecycle
        )

        setContentComposable {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(text = "666")

            }
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onDestroy() {
        super.onDestroy()
        detachEcosed()
    }
}