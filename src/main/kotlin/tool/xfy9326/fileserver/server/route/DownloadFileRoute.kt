package tool.xfy9326.fileserver.server.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tool.xfy9326.fileserver.beans.IConfig
import tool.xfy9326.fileserver.utils.FileManager
import tool.xfy9326.fileserver.utils.FileManager.Companion.joinToPath
import java.io.IOException

fun Route.routeDownloadFile(config: IConfig, fileManager: FileManager) {
    get("/$PATH_FILE/{$PARAMS_PATH_FILE...}") {
        try {
            val paramsPath = call.parameters.getAll(PARAMS_PATH_FILE)
            if (paramsPath.isNullOrEmpty()) {
                call.respondRedirect("/$PATH_FILE/".encodeURLPath(), true)
            } else {
                val path = paramsPath.joinToPath()
                if (fileManager.hasFile(path)) {
                    val file = fileManager.getFile(path)
                    call.response.header(
                        HttpHeaders.ContentDisposition,
                        ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, file.name).toString()
                    )
                    call.respondFile(file)
                } else {
                    call.respondRedirect("/$PATH_FILE/$path/".encodeURLPath(), true)
                }
            }
        } catch (e: AccessDeniedException) {
            e.printStackTrace()
        } catch (e: FileSystemException) {
            e.printStackTrace()
        } catch (e: IOException) {
            if (!config.ignoreUploadDownloadIOException) e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
            if (call.response.status() == null) {
                call.respondException(HttpStatusCode.InternalServerError, e)
            }
        }
    }
}