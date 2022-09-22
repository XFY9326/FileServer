package tool.xfy9326.fileserver.server

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import tool.xfy9326.fileserver.beans.IConfig
import tool.xfy9326.fileserver.server.route.configureRouting

fun launchServer(config: IConfig) {
    embeddedServer(CIO, host = config.host, port = config.port) {
        if (config.callLogging) {
            install(CallLogging)
        }
        if (config.noCache) {
            install(CachingHeaders) {
                options { _, _ ->
                    CachingOptions(CacheControl.NoCache(null))
                }
            }
        }
        configureSecurity(config)
        configureRouting(config)
    }.start(wait = true)
}