package io.ecosed.droid.ui.navigation

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/TermPlux/TermPlux-App
 * 时间: 2023/04/23
 * 描述: Compose导航界面类型枚举类
 * 文档: https://kotlinlang.org/docs/enum-classes.html
 */
enum class ScreenType {
    // Compose的声明式界面
    Compose,
    // Fragment的View界面
    Fragment,
    // 间隔符
    Divider,
    // 分组标题
    Title
}