package io.termplux.model

import com.blankj.utilcode.util.AppUtils

class AppsModel constructor(
    val pkgName: String
) {

    private val mPkgName: String = pkgName

    override fun toString(): String {
        val packageName: String = mPkgName
        val labelName: String = AppUtils.getAppName(packageName)
        return "Application\t{\nPackageName\t=\t$packageName,\nLabel\t=\t$labelName\n}"
    }
}