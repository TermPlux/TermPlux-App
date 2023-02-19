package io.termplux.library

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

fun android(context: Context, int: Int) {

    when (int) {
        androidCamera -> {
            try {
                val intent = Intent("android.media.action.STILL_IMAGE_CAMERA")
                intent.flags =
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("Camera", "camera", e)
            }
        }
        androidContacts -> {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.type = "vnd.android.cursor.dir/contact"
                intent.flags =
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("Contacts", "contacts", e)
            }
        }
        androidDialer -> {
            try {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.flags =
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("error", "dialer", e)
            }
        }
        androidDocuments -> {

        }
        androidGoogle -> {
            try {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                val cn = ComponentName(
                    "com.google.android.googlequicksearchbox",
                    "com.google.android.googlequicksearchbox.SearchActivity"
                )
                intent.component = cn
                context.startActivity(intent)
            } catch (e: Exception) {
                val uri: Uri = Uri.parse("https://www.google.com")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }
        }
        androidGmsCore -> {

        }
        androidPlayStore -> {

        }
        androidSettingsSystem -> {

        }
        androidSettingsDefaultHomeSettings -> {

        }
        androidSettingsDeviceInfo -> {

        }
        androidUninstaller -> {

        }
        else -> {

        }
    }
}

private fun openApplicationMarket(context: Context, packageName: String) {
    val str = "market://details?id=$packageName"
    val localIntent = Intent(Intent.ACTION_VIEW)
    localIntent.data = Uri.parse(str)
    context.startActivity(localIntent)
}


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