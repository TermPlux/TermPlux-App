package io.ecosed.libecosed_example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.ecosed.libecosed.LibEcosedBuilder
import io.ecosed.libecosed.LibEcosedImpl
import io.ecosed.libecosed_example.ui.theme.LibEcosedTheme
import io.ecosed.plugin.EcosedPlugin
import io.ecosed.plugin.EcosedPluginEngine
import io.ecosed.plugin.PluginEngineBuilder

class MainActivity : ComponentActivity(), LibEcosedImpl by LibEcosedBuilder  {

    private lateinit var engine: EcosedPluginEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        engine = PluginEngineBuilder().init(activity = this@MainActivity).build()
        engine.attach()
        engine.addPlugin(plugin = libecosedPlugin)



        engine.execMethodCall(
            channel = libecosedChannel,
            call = ""
        )


        setContent {
            LibEcosedTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        engine.removePlugin(plugin = libecosedPlugin)
        engine.detach()
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun Greeting() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "LibEcosed Example")
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        paddingValues = innerPadding
                    )
            ) {

            }
        }
    }

    @Preview
    @Composable
    private fun GreetingPreview() {
        LibEcosedTheme {
            Greeting()
        }
    }
}