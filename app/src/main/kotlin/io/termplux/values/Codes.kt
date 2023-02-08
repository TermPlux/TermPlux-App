package io.termplux.values

object Codes {
    // 权限申请
    const val requestCodeShizuku: Int = 0
    const val requestCodePickApks: Int = 1
    const val requestCodeReadExternalStorage: Int = 2
    const val requestCodeFakePackageSignature: Int = 3
    const val requestCodeBind: Int = 4
    const val requestCodeGoogle: Int = 5
    const val requestCodePlay: Int = 6

    const val modeNone: Int = 0
    const val modeShizuku: Int = 1

    // 安卓应用
    const val androidCamera: Int = 0
    const val androidContacts: Int = 1
    const val androidDialer: Int = 2
    const val androidDocuments: Int = 3
    const val androidGoogle: Int = 4
    const val androidGmsCore: Int = 5
    const val androidPlayStore: Int = 6
    const val androidSettingsSystem: Int = 7
    const val androidSettingsDefaultHomeSettings: Int = 8
    const val androidSettingsDeviceInfo: Int = 9
    const val androidUninstaller: Int = 10
}