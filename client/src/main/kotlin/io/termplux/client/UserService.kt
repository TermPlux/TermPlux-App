package io.termplux.client

import io.termplux.aidl.IUserService
import kotlin.system.exitProcess

internal class UserService : IUserService.Stub() {

    override fun destroy() {
        exitProcess(0)
    }

    override fun exit() {
        exitProcess(0)
    }
}