package tool.xfy9326.fileserver

import tool.xfy9326.fileserver.beans.BasicConfig
import tool.xfy9326.fileserver.server.launchServer

object ApplicationTest {
    @JvmStatic
    fun main(args: Array<String>) {
        launchServer(BasicConfig())
    }
}