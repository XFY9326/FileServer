package tool.xfy9326.fileserver.server.route

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import tool.xfy9326.fileserver.beans.IConfig
import tool.xfy9326.fileserver.utils.FileManager
import java.io.File
import java.io.IOException

fun Route.routeUploadFile(config: IConfig, fileManager: FileManager) {
    put("/$PATH_FILE/{$PARAMS_PATH_FILE...}") {
        val path = call.getParamsPath()
        try {
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
        } catch (e: IOException) {
            if (!config.ignoreUploadDownloadIOException) e.printStackTrace()
            call.respond("$path: Upload file failed!")
        } catch (e: Exception) {
            e.printStackTrace()
            call.respondException(HttpStatusCode.InternalServerError, e)
        }
    }
    post("/$PATH_FILE/{$PARAMS_PATH_FILE...}") {
        uploadMultipleFiles(config, fileManager)
    }
    post("/$PATH_FILE/{$PARAMS_PATH_FILE...}/") {
        uploadMultipleFiles(config, fileManager)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.uploadMultipleFiles(config: IConfig, fileManager: FileManager) {
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
                        val filePath = if (path.isEmpty()) fileName else "$path${File.separator}$fileName"
                        try {
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
                        } catch (e: IOException) {
                            if (!config.ignoreUploadDownloadIOException) e.printStackTrace()
                            output.append("$filePath: Upload file failed!").append("\r\n")
                        }
                    }
                }
                index += 1
            }
            call.respond(output.toString())
        }
    } catch (e: Exception) {
        e.printStackTrace()
        call.respondException(HttpStatusCode.InternalServerError, e)
    }
}