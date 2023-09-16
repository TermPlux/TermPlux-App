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
package io.ecosed.libecosed_example

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.ecosed.droid.app.EcosedActivity
import io.ecosed.droid.app.EcosedActivityImpl
import io.ecosed.droid.app.EcosedLauncher
import io.ecosed.droid.app.NavBarBackgroundColor

@EcosedLauncher(isLauncher = true)
@NavBarBackgroundColor(color = Color.TRANSPARENT)
class MyActivity : AppCompatActivity(), EcosedActivityImpl by EcosedActivity<MyActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachEcosed(activity = this@MyActivity, lifecycle = lifecycle)
    }

    override fun onDestroy() {
        super.onDestroy()
        detachEcosed(lifecycle = lifecycle)
    }
}