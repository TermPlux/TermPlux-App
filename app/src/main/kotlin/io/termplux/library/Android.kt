package io.termplux.library

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import io.termplux.values.Codes

fun android(context: Context, int: Int) {

    when (int) {
        Codes.androidCamera -> {
            try {
                val intent = Intent("android.media.action.STILL_IMAGE_CAMERA")
                intent.flags =
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("Camera", "camera", e)
            }
        }
        Codes.androidContacts -> {
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
        Codes.androidDialer -> {
            try {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.flags =
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("error", "dialer", e)
            }
        }
        Codes.androidDocuments -> {

        }
        Codes.androidGoogle -> {
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
        Codes.androidGmsCore -> {

        }
        Codes.androidPlayStore -> {

        }
        Codes.androidSettingsSystem -> {

        }
        Codes.androidSettingsDefaultHomeSettings -> {

        }
        Codes.androidSettingsDeviceInfo -> {

        }
        Codes.androidUninstaller -> {

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