package tool.xfy9326.fileserver.server

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import tool.xfy9326.fileserver.beans.IConfig
import tool.xfy9326.fileserver.server.route.configureRouting

fun launchServer(config: IConfig) {
    embeddedServer(CIO, host = config.host, port = config.port) {
        if (config.callLogging) {
            install(CallLogging)
        }
        configureSecurity(config)
        configureRouting(config)
    }.start(wait = true)
}