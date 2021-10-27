package tool.xfy9326.fileserver.server.route

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import tool.xfy9326.fileserver.utils.FileManager

const val PARAM_URL = "url"

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
        val showUrl = call.parameters[PARAM_URL]
        val files = fileManager.listFiles(call.getParamsPath())
        val output = buildString {
            files.forEach {
                if (showUrl != null) {
                    append(call.url {
                        encodedPath += if (encodedPath.endsWith("/")) {
                            "/$it"
                        } else {
                            it
                        }.encodeURLPath()
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
    } catch (e: NoSuchFileException) {
        call.respondException(HttpStatusCode.NotFound, e)
    } catch (e: IllegalStateException) {
        call.respondException(HttpStatusCode.BadRequest, e)
    } catch (e: Exception) {
        e.printStackTrace()
        call.respond(HttpStatusCode.InternalServerError)
    }
}