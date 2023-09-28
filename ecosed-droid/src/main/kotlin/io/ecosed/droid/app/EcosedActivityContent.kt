package io.ecosed.droid.app

interface EcosedActivityContent {
    var parent: (() -> Unit)?
    var isLauncher: Boolean?
    var body: (() -> Unit)?
}