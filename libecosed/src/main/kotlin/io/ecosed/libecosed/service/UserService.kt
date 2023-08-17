package io.ecosed.libecosed.service

import io.ecosed.libecosed.IUserService
import kotlin.system.exitProcess

internal class UserService : IUserService.Stub() {

    override fun destroy() {
        exitProcess(0)
    }

    override fun exit() {
        exitProcess(0)
    }
}