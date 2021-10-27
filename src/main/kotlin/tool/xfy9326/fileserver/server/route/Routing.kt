package tool.xfy9326.fileserver.server.route

import io.ktor.application.*
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
        withAuth(config) {
            routeListFile(fileManager)
            routeViewFile(fileManager)
            routeUploadFile(fileManager)
        }
    }
}
