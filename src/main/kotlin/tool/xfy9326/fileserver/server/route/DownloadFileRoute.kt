package tool.xfy9326.fileserver.server.route

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tool.xfy9326.fileserver.utils.FileManager
import tool.xfy9326.fileserver.utils.FileManager.Companion.joinToPath

fun Route.routeDownloadFile(fileManager: FileManager) {
    get("/$PATH_FILE/{$PARAMS_PATH_FILE...}") {
        try {
            val paramsPath = call.parameters.getAll(PARAMS_PATH_FILE)
            if (paramsPath.isNullOrEmpty()) {
                call.respondRedirect("/$PATH_FILE/".encodeURLPath(), true)
            } else {
                val path = paramsPath.joinToPath()
                if (fileManager.hasFile(path)) {
                    call.respondBytesWriter {
                        fileManager.readFile(path, this)
                    }
                } else {
                    call.respondRedirect("/$PATH_FILE/$path/".encodeURLPath(), true)
                }
            }
        } catch (e: AccessDeniedException) {
            call.respondException(HttpStatusCode.Forbidden, e)
        } catch (e: FileSystemException) {
            call.respondException(HttpStatusCode.BadRequest, e)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}