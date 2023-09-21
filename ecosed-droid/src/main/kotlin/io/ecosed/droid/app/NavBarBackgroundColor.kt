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
package io.ecosed.droid.app

import android.graphics.Color
import androidx.annotation.ColorInt

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/ecosed/EcosedDroid
 * 时间: 2023/09/15
 * 描述: 导航栏着色注解
 * 文档: https://github.com/ecosed/EcosedDroid/blob/master/README.md
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class NavBarBackgroundColor(@ColorInt val color: Int = Color.TRANSPARENT)