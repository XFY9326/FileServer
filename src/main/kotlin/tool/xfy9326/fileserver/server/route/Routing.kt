package tool.xfy9326.fileserver.server.route

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tool.xfy9326.fileserver.beans.IConfig
import tool.xfy9326.fileserver.utils.FileManager

const val PARAMS_PATH_FILE = "path"
const val PATH_LIST = "l"
const val PATH_FILE = "f"

fun Application.configureRouting(config: IConfig) {
    val fileManager = FileManager(config)

    routing {
        get("/") {
            call.respondText("Server OK!\r\n")
        }
        get("/logout") {
            call.respond(HttpStatusCode.Unauthorized)
        }
        withAuth(!config.allowAnonymous) {
            routeListFile(fileManager)
            routeViewFile(config, fileManager)
            routeUploadFile(fileManager)
        }
        withAuth(!config.allowAnonymous && (config.allowAnonymous || !config.allowAnonymousDownload)) {
            routeDownloadFile(fileManager)
        }
    }
}
