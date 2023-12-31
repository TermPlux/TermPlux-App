package io.termplux.app

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.appbar.MaterialToolbar
import io.termplux.app.databinding.ContainerBinding
import io.termplux.base.TPBaseActivity
import io.termplux.ui.view.UI

class MainActivity : TPBaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ContainerBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ContainerBinding.inflate(layoutInflater)


        val toolbar = MaterialToolbar(this)

        setSupportActionBar(toolbar)

        val mFragmentContainerView = binding.navHostFragmentContentMain

        val navHostFragment =
            (supportFragmentManager.findFragmentById(mFragmentContainerView.id) as NavHostFragment?)!!
        navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_graph, Bundle())

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        val ui = UI.build()

        val content = ui.content(
            context = this
        )

        setContentView(mFragmentContainerView)

        navController.navigate(R.id.nav_launcher)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}