package io.ecosed.libecosed_example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.ecosed.libecosed.LibEcosedPlugin
import io.ecosed.libecosed_example.ui.theme.LibEcosedTheme
import io.ecosed.plugin.EcosedPluginEngine
import io.ecosed.plugin.PluginEngineBuilder

class MainActivity : ComponentActivity() {

    private lateinit var engine: EcosedPluginEngine
    private lateinit var plugin: LibEcosedPlugin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        engine = PluginEngineBuilder().init(activity = this@MainActivity).build()
        plugin = LibEcosedPlugin()
        engine.attach()
        engine.addPlugin(plugin = plugin)


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
        engine.removePlugin(plugin = plugin)
        engine.detach()
    }

    @Composable
    private fun Greeting() {

    }

    @Preview
    @Composable
    private fun GreetingPreview() {
        LibEcosedTheme {
            Greeting()
        }
    }
}