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

import android.app.Application
import io.ecosed.droid.app.IEcosedApplication
import io.ecosed.droid.app.EcosedApplication
import io.ecosed.droid.app.EcosedHost
import io.ecosed.droid.plugin.EcosedClient

class MyApplication : Application(), IEcosedApplication by EcosedApplication<MyApplication>() {

    override fun onCreate() {
        super.onCreate()

        attachUtils(
            application = this@MyApplication,
            host = object : EcosedHost {

                override fun isDebug(): Boolean {
                    return BuildConfig.DEBUG
                }

            }
        )

    }

    override fun init() {
        log("init")
    }

    override fun initSDKs() {
        log("initSDKs")
    }

    override fun initSDKInitialized() {
        log("initSDKInitialized")
    }

    override fun getEcosedClient(): EcosedClient {
        return MyClient()
    }
}