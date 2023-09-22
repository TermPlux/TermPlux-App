package io.ecosed.droid.app

import android.graphics.drawable.Drawable

interface EcosedHost {

    fun isDebug(): Boolean
 //   fun getProductLogo(): Drawable?
    fun getPluginList(): ArrayList<EcosedPlugin>?

}