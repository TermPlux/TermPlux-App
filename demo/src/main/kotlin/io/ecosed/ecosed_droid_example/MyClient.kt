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

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import io.ecosed.droid.LibEcosedBuilder
import io.ecosed.droid.LibEcosedImpl
import io.ecosed.droid.plugin.EcosedClient
import io.ecosed.droid.app.EcosedPlugin
import io.ecosed.droid.plugin.LibEcosed

class MyClient : EcosedClient(), LibEcosedImpl by LibEcosedBuilder {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return TextView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (view as TextView).text = "Hello World!"
    }

    override fun getPluginList(): ArrayList<EcosedPlugin> {
        return arrayListOf(DemoPlugin())
    }

    override fun getLibEcosed(): LibEcosed {
        super.getLibEcosed()
        return mLibEcosed
    }

    override fun getProductLogo(): Drawable? {
        super.getProductLogo()
        return ContextCompat.getDrawable(this, R.drawable.baseline_keyboard_command_key_24)
    }

    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }


    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }
}