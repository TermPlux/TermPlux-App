/**
 * Copyright EcosedDroid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.ecosed.example

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import io.ecosed.embedding.EcosedActivity
import io.ecosed.embedding.IEcosedActivity
import io.ecosed.example.ui.theme.EDExampleTheme

class DemoActivity : AppCompatActivity(),
    IEcosedActivity by EcosedActivity<DemoApplication, DemoActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachEcosed(
            activity = this@DemoActivity,
            lifecycle = lifecycle
        )
//        setContent {
//            EDExampleTheme {
//                Greeting()
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detachEcosed()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Greeting() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(
                                id = R.string.app_name
                            )
                        )
                    }
                )
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        paddingValues = innerPadding
                    ),
                color = MaterialTheme.colorScheme.background
            ) {
                Column {
                    Button(
                        onClick = {
                            Toast.makeText(
                                this@DemoActivity,
                                execMethodCall<String>(
                                    channel = "ecosed_droid",
                                    method = "shizuku_version"
                                ) ?: "null",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Text(text = "Shizuku版本")
                    }
                }
            }
        }
    }

    @Preview(
        showBackground = true,
        device = "id:pixel_7_pro",
        showSystemUi = true,
        wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE
    )
    @Composable
    private fun GreetingPreview() {
        EDExampleTheme {
            Greeting()
        }
    }
}