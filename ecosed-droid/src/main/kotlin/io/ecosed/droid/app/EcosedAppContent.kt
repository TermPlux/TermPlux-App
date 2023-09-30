package io.ecosed.droid.app

interface EcosedAppContent {
    var parent: (() -> Unit)?
    var host: EcosedHost?
    var initialize: EcosedAppInitialize?
    var body: (() -> Unit)?
}