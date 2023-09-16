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

import android.app.Application
import io.ecosed.droid.app.EcosedApplicationImpl
import io.ecosed.droid.app.EcosedApplication
import io.ecosed.droid.plugin.EcosedClient
import io.ecosed.droid.plugin.EcosedEngine

class MyApplication : Application(), EcosedApplicationImpl by EcosedApplication<MyApplication>() {

    override fun onCreate() {
        super.onCreate()
        attachUtils(application = this@MyApplication)
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

    override fun getPluginEngine(): EcosedEngine {
        return engine
    }

    override fun getEcosedClient(): EcosedClient {
        return MyClient()
    }
}