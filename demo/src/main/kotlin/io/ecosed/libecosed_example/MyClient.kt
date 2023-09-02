package io.ecosed.libecosed_example

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import io.ecosed.libecosed.LibEcosedBuilder
import io.ecosed.libecosed.LibEcosedImpl
import io.ecosed.plugin.EcosedClient
import io.ecosed.plugin.EcosedPlugin
import io.ecosed.plugin.LibEcosed

class MyClient : EcosedClient(), LibEcosedImpl by LibEcosedBuilder {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun getPluginList(): ArrayList<EcosedPlugin> {
        return arrayListOf()
    }

    override fun getLibEcosed(): LibEcosed {
        super.getLibEcosed()
        return mLibEcosed
    }

    override fun getProductLogo(): Drawable? {
        super.getProductLogo()
        return ContextCompat.getDrawable(this, R.drawable.baseline_keyboard_command_key_24)
    }

    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }
}