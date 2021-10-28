package tool.xfy9326.fileserver.server.route

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import tool.xfy9326.fileserver.utils.FileManager
import java.io.File

fun Route.routeUploadFile(fileManager: FileManager) {
    put("/$PATH_FILE/{$PARAMS_PATH_FILE...}") {
        try {
            val path = call.getParamsPath()
            fileManager.saveFile(path, call.receiveChannel())
            call.respond("$path: Upload file success!")
        } catch (e: AccessDeniedException) {
            call.respondException(HttpStatusCode.Forbidden, e)
        } catch (e: FileAlreadyExistsException) {
            call.respondException(HttpStatusCode.Conflict, e)
        } catch (e: FileSystemException) {
            call.respondException(HttpStatusCode.InternalServerError, e)
        } catch (e: IllegalStateException) {
            call.respondException(HttpStatusCode.NotAcceptable, e)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
    post("/$PATH_FILE/{$PARAMS_PATH_FILE...}") {
        uploadMultipleFiles(fileManager)
    }
    post("/$PATH_FILE/{$PARAMS_PATH_FILE...}/") {
        uploadMultipleFiles(fileManager)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.uploadMultipleFiles(fileManager: FileManager) {
    try {
        val path = call.getParamsPath()
        if (fileManager.hasFile(path)) {
            call.respond(HttpStatusCode.Conflict, "$path: Parent directory is a file!")
        } else {
            val output = StringBuilder()
            var index = 0
            call.receiveMultipart().forEachPart {
                if (it is PartData.FileItem) {
                    val fileName = it.name ?: it.originalFileName
                    if (fileName == null) {
                        output.append("Upload file[$index] doesn't has a name!").append("\r\n")
                    } else {
                        try {
                            val filePath = if (path.isEmpty()) fileName else "$path${File.separator}$fileName"
                            it.streamProvider().use { input ->
                                fileManager.saveFile(filePath, input)
                            }
                            output.append("$filePath: Upload file success!").append("\r\n")
                        } catch (e: AccessDeniedException) {
                            output.append(e.message.toString()).append("\r\n")
                        } catch (e: FileAlreadyExistsException) {
                            output.append(e.message.toString()).append("\r\n")
                        } catch (e: FileSystemException) {
                            output.append(e.message.toString()).append("\r\n")
                        }
                    }
                }
                index += 1
            }
            call.respond(output.toString())
        }
    } catch (e: Exception) {
        e.printStackTrace()
        call.respond(HttpStatusCode.InternalServerError)
    }
}