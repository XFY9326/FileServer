package tool.xfy9326.fileserver.server

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import tool.xfy9326.fileserver.beans.IConfig
import tool.xfy9326.fileserver.server.route.configureRouting

fun launchServer(config: IConfig) {
    embeddedServer(CIO, host = config.host, port = config.port) {
        if (config.callLogging) {
            install(CallLogging)
        }
        if (config.noCache) {
            install(CachingHeaders) {
                options {
                    CachingOptions(CacheControl.NoCache(null))
                }
            }
        }
        configureSecurity(config)
        configureRouting(config)
    }.start(wait = true)
}