package io.ecosed.droid.service

import io.ecosed.droid.IUserService
import kotlin.system.exitProcess

internal class UserService : IUserService.Stub() {

    override fun destroy() {
        exitProcess(0)
    }

    override fun exit() {
        exitProcess(0)
    }
}