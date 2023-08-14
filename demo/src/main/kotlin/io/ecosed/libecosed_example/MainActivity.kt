package io.ecosed.libecosed_example

import android.os.Bundle
import android.widget.Toast
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
import io.ecosed.plugin.PluginEngine

class MainActivity : ComponentActivity(), LibEcosedImpl by LibEcosedBuilder  {

    private lateinit var engine: PluginEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        engine = PluginEngine.build(context = this@MainActivity)
        engine.attach()
        engine.addPlugin(libecosedPlugin)


        engine.execMethodCall(
            name = libecosedChannel,
            method = "text"
        )?.let {
            Toast.makeText(
                this@MainActivity,
                it.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }


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
        engine.removePlugin(libecosedPlugin)
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