package io.ecosed.libecosed.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.ecosed.libecosed.plugin.LibEcosed
import io.ecosed.libecosed.ui.theme.LibEcosedTheme
import io.ecosed.plugin.execMethodCall

internal class ManagerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibEcosedTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Greeting() {
        Scaffold { innerPadding ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding)) {
                Button(
                    onClick = {
                        execMethodCall(
                            activity = this@ManagerActivity,
                            name = LibEcosed.channel,
                            method = LibEcosed.launchApp
                        )
                    }
                ) {
                    Text(text = "Launch App")
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun GreetingPreview() {
        LibEcosedTheme {
            Greeting()
        }
    }
}