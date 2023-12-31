package io.termplux.app

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.appbar.MaterialToolbar
import io.termplux.app.databinding.ContainerBinding
import io.termplux.base.TPBaseActivity
import io.termplux.ui.view.UI

class MainActivity : TPBaseActivity() {

    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mBinding: ContainerBinding
    private lateinit var mNavController: NavController
    private lateinit var mBundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ContainerBinding.inflate(layoutInflater)


        val toolbar = MaterialToolbar(this)

        setSupportActionBar(toolbar)

        val mFragmentContainerView = mBinding.navHostFragmentContentMain

        mBundle = Bundle()

        val navHostFragment =
            (supportFragmentManager.findFragmentById(mFragmentContainerView.id) as NavHostFragment?)!!
        mNavController = navHostFragment.navController
        mNavController.setGraph(R.navigation.nav_graph, mBundle)

        mAppBarConfiguration = AppBarConfiguration(mNavController.graph)
        setupActionBarWithNavController(mNavController, mAppBarConfiguration)


        val ui = UI.build()

        val content = ui.content(
            context = this,
            container = mFragmentContainerView
        )

        setContentView(content)

        //mNavController.navigate(R.id.nav_launcher)
    }

    override fun onSupportNavigateUp(): Boolean {
        return mNavController.navigateUp(mAppBarConfiguration) || super.onSupportNavigateUp()
    }
}