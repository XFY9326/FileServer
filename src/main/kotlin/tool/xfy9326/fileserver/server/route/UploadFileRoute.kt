package tool.xfy9326.fileserver.server.route

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import tool.xfy9326.fileserver.utils.FileManager

fun Route.routeUploadFile(fileManager: FileManager) {
    put("/$PATH_FILE/{$PARAMS_PATH_FILE...}") {
        try {
            fileManager.saveFile(call.getParamsPath(), call.receiveChannel())
            call.respond(HttpStatusCode.Created)
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
}