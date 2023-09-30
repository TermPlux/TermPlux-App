//package io.ecosed.droid.plugin
//
//import android.app.Application
//import io.ecosed.droid.app.EcosedMethodCall
//import io.ecosed.droid.app.EcosedResult
//
//internal class EcosedDroid : LibEcosed() {
//
//    override val channel: String
//        get() = mChannel
//
//    override fun init() {
//
//    }
//
//    override fun initSDKs(application: Application) {
//        super.initSDKs(application)
//    }
//
//    override fun initSDKInitialized() {
//        super.initSDKInitialized()
//    }
//
//
//    override fun onEcosedMethodCall(call: EcosedMethodCall, result: EcosedResult) {
//        when (call.method) {
//            isDebug -> result.success("")
//            else -> result.notImplemented()
//        }
//    }
//
//    internal companion object {
//
//
//        const val mChannel: String = "libecosed"
//
//        const val isDebug: String = "is_debug"
//    }
//}