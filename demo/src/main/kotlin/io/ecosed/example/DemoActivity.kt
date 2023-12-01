/**
 * Copyright EcosedKit
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
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.ecosed.embedding.EcosedActivity
import io.ecosed.embedding.IEcosedActivity
import io.ecosed.example.ui.theme.EcosedKitTheme

class DemoActivity : AppCompatActivity(),
    IEcosedActivity by EcosedActivity<DemoApplication, DemoActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachEcosed(
            activity = this@DemoActivity,
            lifecycle = lifecycle
        )
        setContentSpace { flutter, commit ->
            setContent {
                EcosedKitTheme {
                    Greeting(
                        flutter = flutter,
                        commit = commit
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detachEcosed()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Greeting(flutter: View? = null, commit: () -> Unit = {}) {
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
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            all = 12.dp
                        )
                ) {
                    flutter?.let { view ->
                        AndroidView(
                            factory = {
                                return@AndroidView view
                            },
                            modifier = Modifier.fillMaxSize(),
                            update = {
                                commit()
                            }
                        )
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
        EcosedKitTheme {
            Greeting()
        }
    }
}