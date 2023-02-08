package io.termplux.values

import io.termplux.BuildConfig

object Packages {
    // 此app
    const val termPlux: String = BuildConfig.APPLICATION_ID

    // Shizuku
    const val shizuku: String = "moe.shizuku.privileged.api"

    // Linux运行环境
    const val termux: String = "com.termux"

    // 谷歌基础服务
    const val gms: String = "com.google.android.gms"
    const val google: String = "com.google.android.googlequicksearchbox"
}