package tool.xfy9326.fileserver.server.route

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tool.xfy9326.fileserver.utils.FileManager
import tool.xfy9326.fileserver.utils.buildViewFileHtml

fun Route.routeViewFile(fileManager: FileManager) {
    get("/$PATH_FILE/{$PARAMS_PATH_FILE...}") {
        try {
            val paramsPath = call.parameters.getAll(PARAMS_PATH_FILE)
            if (paramsPath.isNullOrEmpty()) {
                call.respondRedirect("/$PATH_FILE/".encodeURLPath(), true)
            } else {
                val path = paramsPath.joinToString("/")
                if (fileManager.hasFile(path)) {
                    call.respondBytesWriter {
                        fileManager.readFile(path, this)
                    }
                } else {
                    call.respondRedirect("/$PATH_FILE/$path/".encodeURLPath(), true)
                }
            }
        } catch (e: FileSystemException) {
            call.respondException(HttpStatusCode.BadRequest, e)
        } catch (e: IllegalStateException) {
            call.respondException(HttpStatusCode.NotAcceptable, e)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    get("/$PATH_FILE/{$PARAMS_PATH_FILE...}/") {
        try {
            val path = call.getParamsPath()
            val files = fileManager.listFiles(path)
            call.respondHtml {
                buildViewFileHtml("/$path", files, call.principal<UserIdPrincipal>()?.name)
            }
        } catch (e: NoSuchFileException) {
            call.respondException(HttpStatusCode.NotFound, e)
        } catch (e: IllegalStateException) {
            call.respondException(HttpStatusCode.BadRequest, e)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}