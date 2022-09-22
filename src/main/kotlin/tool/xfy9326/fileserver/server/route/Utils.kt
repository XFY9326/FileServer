package tool.xfy9326.fileserver.server.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tool.xfy9326.fileserver.server.DEFAULT_BASIC_AUTH
import tool.xfy9326.fileserver.utils.FileManager.Companion.joinToPath

fun Routing.withAuth(enableAuth: Boolean, block: Route.() -> Unit) {
    if (enableAuth) {
        authenticate(DEFAULT_BASIC_AUTH, build = block)
    } else {
        block()
    }
}

fun ApplicationCall.getParamsPath() = parameters.getAll(PARAMS_PATH_FILE)?.filter { it.isNotBlank() }.joinToPath()

suspend fun ApplicationCall.respondException(statusCode: HttpStatusCode, exception: Exception) {
    respond(statusCode, exception.message.toString() + "\r\n")
}