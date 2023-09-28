package io.ecosed.droid.app

interface EcosedAppContent {
    var parent: (() -> Unit)?
    var host: EcosedAppHost?
    var initialize: EcosedAppInitialize?
    var body: (() -> Unit)?
}