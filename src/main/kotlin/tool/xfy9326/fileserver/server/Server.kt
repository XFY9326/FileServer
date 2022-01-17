package tool.xfy9326.fileserver.server

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.slf4j.event.Level
import tool.xfy9326.fileserver.beans.IConfig
import tool.xfy9326.fileserver.server.route.configureRouting

fun launchServer(config: IConfig) {
    embeddedServer(CIO, host = config.host, port = config.port) {
        if (config.callLogging) {
            install(CallLogging) {
                level = Level.INFO
            }
        }
        configureSecurity(config)
        configureRouting(config)
    }.start(wait = true)
}