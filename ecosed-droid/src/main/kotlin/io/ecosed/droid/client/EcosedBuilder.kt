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
package io.ecosed.droid.client

import android.content.Context

internal class EcosedBuilder {

    /** 上下文 */
    private lateinit var mContext: Context

    /**
     * 传入上下文
     * @param context 上下文
     */
    internal fun init(
        context: Context?
    ): EcosedBuilder {
        context?.let {
            mContext = it
        }
        return this@EcosedBuilder
    }

    /**
     * 构建API
     */
    internal fun build(
        content: EcosedClient.() -> Unit
    ) {
        content(EcosedClient(context = mContext))
    }
}