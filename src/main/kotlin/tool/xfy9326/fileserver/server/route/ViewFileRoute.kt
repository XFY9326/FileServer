package tool.xfy9326.fileserver.server.route

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tool.xfy9326.fileserver.beans.IConfig
import tool.xfy9326.fileserver.utils.FileManager
import tool.xfy9326.fileserver.utils.buildViewFileHtml

fun Route.routeViewFile(config: IConfig, fileManager: FileManager) {
    get("/$PATH_FILE/{$PARAMS_PATH_FILE...}/") {
        try {
            val path = call.getParamsPath()
            val files = fileManager.listFiles(path)
            call.respondHtml {
                buildViewFileHtml("/$path", files, call.principal<UserIdPrincipal>()?.name, config.allowAnonymous)
            }
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
}