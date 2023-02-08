package io.termplux.services

import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import io.termplux.activity.MainActivity

@RequiresApi(Build.VERSION_CODES.N)
class QuickSettings: TileService() {

    override fun onClick() {
        super.onClick()
        val intent = Intent(applicationContext, MainActivity().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
    }
}