package tool.xfy9326.fileserver.server.route

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import tool.xfy9326.fileserver.utils.FileManager

const val PARAM_URL = "url"
const val PARAM_U = "u"
const val PARAM_FILE = "file"
const val PARAM_F = "f"

fun Route.routeListFile(fileManager: FileManager) {
    get("/$PATH_LIST/{$PARAMS_PATH_FILE...}/") {
        listFiles(fileManager)
    }
    get("/$PATH_LIST/{$PARAMS_PATH_FILE...}") {
        listFiles(fileManager)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.listFiles(fileManager: FileManager) {
    try {
        val showUrl = call.parameters[PARAM_URL] ?: call.parameters[PARAM_U]
        val fileOnly = (call.parameters[PARAM_FILE] ?: call.parameters[PARAM_F]) != null
        val files = fileManager.listFiles(call.getParamsPath(), fileOnly)
        val output = buildString {
            files.forEach {
                if (showUrl != null) {
                    append(call.url {
                        encodedPath = encodedPath.trimEnd('/') + "/" + it
                    }.substringBefore("?"))
                    if (it.endsWith("/")) {
                        append("?$PARAM_URL=$showUrl")
                    }
                } else {
                    append(it)
                }
                append("\r\n")
            }
        }
        call.respondText(output + "\r\n")
    } catch (e: AccessDeniedException) {
        call.respondException(HttpStatusCode.Forbidden, e)
    } catch (e: NoSuchFileException) {
        call.respondException(HttpStatusCode.NotFound, e)
    } catch (e: IllegalStateException) {
        call.respondException(HttpStatusCode.BadRequest, e)
    } catch (e: Exception) {
        e.printStackTrace()
        call.respond(HttpStatusCode.InternalServerError)
    }
}