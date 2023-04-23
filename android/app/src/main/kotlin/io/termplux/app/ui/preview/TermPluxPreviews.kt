package io.termplux.app.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

// 手机竖屏状态预览
// 设备: Google Pixel 6 Pro
// 版本: Android 13
// 语言: 中国大陆简体中文
// 显示: 字体一倍大小，显示系统界面，显示背景，背景色为白色，支持系统界面模式
@Preview(
    name = "Compact",
    group = "TermPlux",
    device = "id:pixel_6_pro",
    apiLevel = 33,
    locale = "zh-rCN",
    fontScale = 1.0f,
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_UNDEFINED
)
// 手机横屏状态/折叠屏展开状态预览
// 设备: Google Pixel 6 Pro
// 版本: Android 13
// 语言: 中国大陆简体中文
// 显示: 字体一倍大小，显示系统界面，显示背景，背景色为白色，支持系统界面模式
@Preview(
    name = "Medium",
    group = "TermPlux",
    device = "spec:parent=pixel_6_pro,orientation=landscape",
    apiLevel = 33,
    locale = "zh-rCN",
    fontScale = 1.0f,
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_UNDEFINED
)
// 平板预览
// 设备: Google Pixel 6 Pro
// 版本: Android 13
// 语言: 中国大陆简体中文
// 显示: 字体一倍大小，显示系统界面，显示背景，背景色为白色，支持系统界面模式
@Preview(
    name = "Expanded",
    group = "TermPlux",
    device = "id:Nexus 10",
    apiLevel = 33,
    locale = "zh-rCN",
    fontScale = 1.0f,
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_UNDEFINED
)
/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/TermPlux/TermPlux-App
 * 时间: 2023/04/23
 * 描述: Compose界面预览类
 * 文档: https://developer.android.google.cn/jetpack/compose/tooling/previews
 */
annotation class TermPluxPreviews