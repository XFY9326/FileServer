package tool.xfy9326.fileserver.server.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.jvm.javaio.*
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
        get("/favicon.ico") {
            javaClass.classLoader.getResourceAsStream("favicon.ico")?.use {
                call.respondBytesWriter(ContentType.Image.XIcon, HttpStatusCode.OK) {
                    it.copyTo(this)
                }
            } ?: call.respond(HttpStatusCode.NotFound)
        }
        withAuth(!config.allowAnonymous) {
            routeListFile(fileManager)
            routeViewFile(config, fileManager)
            routeUploadFile(config, fileManager)
        }
        withAuth(!config.allowAnonymous && (config.allowAnonymous || !config.allowAnonymousDownload)) {
            routeDownloadFile(config, fileManager)
        }
    }
}
