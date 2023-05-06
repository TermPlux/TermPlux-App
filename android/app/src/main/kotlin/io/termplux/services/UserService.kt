package io.termplux.services

import io.termplux.IUserService
import kotlin.system.exitProcess

class UserService : IUserService.Stub() {

    override fun destroy() {
        exitProcess(0)
    }

    override fun exit() {
        exitProcess(0)
    }
}