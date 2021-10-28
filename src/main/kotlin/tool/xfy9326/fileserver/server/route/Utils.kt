package tool.xfy9326.fileserver.server.route

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tool.xfy9326.fileserver.beans.IConfig
import tool.xfy9326.fileserver.server.DEFAULT_BASIC_AUTH
import tool.xfy9326.fileserver.utils.FileManager.Companion.joinToPath

fun Routing.withAuth(config: IConfig, block: Route.() -> Unit) {
    if (config.allowAnonymous) {
        block()
    } else {
        authenticate(DEFAULT_BASIC_AUTH) {
            block()
        }
    }
}

fun ApplicationCall.getParamsPath() = parameters.getAll(PARAMS_PATH_FILE)?.filter { it.isBlank() }.joinToPath()

suspend fun ApplicationCall.respondException(statusCode: HttpStatusCode, exception: Exception) {
    respond(statusCode, exception.message.toString() + "\r\n")
}