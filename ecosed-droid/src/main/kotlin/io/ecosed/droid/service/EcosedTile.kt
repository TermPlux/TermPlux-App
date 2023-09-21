package io.ecosed.droid.service

import android.content.Intent
import android.service.quicksettings.TileService


internal class EcosedTile : TileService() {

    override fun onClick() {
        super.onClick()
//        val intent = Intent(applicationContext, MainActivity().javaClass)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        if (!isLocked) {
//            startActivityAndCollapse(intent)
//        } else unlockAndRun {
//            startActivityAndCollapse(intent)
//        }
    }
}