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
package io.ecosed.embedding

/**
 * 方法调用结果回调.
 */
interface EcosedResult {

    /**
     * 处理成功结果.
     * @param result 处理成功结果,注意可能为空.
     */
    fun success(result: Any?)

    /**
     * 处理错误结果.
     * @param errorCode 错误代码.
     * @param errorMessage 错误消息,注意可能为空.
     * @param errorDetails 详细信息,注意可能为空.
     */
    fun error(
        errorCode: String,
        errorMessage: String?,
        errorDetails: Any?
    ): Nothing

    /**
     * 处理对未实现方法的调用.
     */
    fun notImplemented()
}