package io.ecosed.libecosed.activity

import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import rikka.material.app.MaterialActivity

class SettingsActivity : MaterialActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            FragmentContainerView(
                context = this@SettingsActivity
            ).apply {
                if (savedInstanceState == null){

                }
            }
        )
    }
}