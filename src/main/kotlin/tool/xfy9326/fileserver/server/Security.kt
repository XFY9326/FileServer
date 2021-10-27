package tool.xfy9326.fileserver.server

import io.ktor.application.*
import io.ktor.auth.*
import tool.xfy9326.fileserver.beans.IConfig

const val DEFAULT_BASIC_AUTH = "auth-basic"

fun Application.configureSecurity(config: IConfig) {
    install(Authentication) {
        basic(DEFAULT_BASIC_AUTH) {
            validate {
                if (it.name in config.users && config.users[it.name] == it.password) {
                    UserIdPrincipal(it.name)
                } else {
                    null
                }
            }
        }
    }
}
