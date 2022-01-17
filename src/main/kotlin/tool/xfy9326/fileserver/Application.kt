package tool.xfy9326.fileserver

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import tool.xfy9326.fileserver.beans.BasicConfig
import tool.xfy9326.fileserver.beans.IConfig
import tool.xfy9326.fileserver.server.launchServer
import tool.xfy9326.fileserver.utils.ConfigManager
import java.io.File

fun main(args: Array<String>) {
    JarCommand().subcommands(Launch(), DefaultConfig()).main(args)
}

private class JarCommand : NoOpCliktCommand(name = "java -jar FileServer-<VERSION>.jar")

private class Launch : CliktCommand(help = "Launch file server"), IConfig {
    override val host: String by option(help = "Listening host").default(BasicConfig.DEFAULT_HOST)
    override val port: Int by option(help = "Listening port").int().default(BasicConfig.DEFAULT_PORT)
    override val root: String by option(help = "File server root").default(BasicConfig.DEFAULT_ROOT)
    override val allowAnonymous: Boolean = BasicConfig.DEFAULT_ALLOW_ANONYMOUS
    override val allowAnonymousDownload: Boolean = BasicConfig.DEFAULT_ALLOW_ANONYMOUS_DOWNLOAD
    override val users: Map<String, String> = BasicConfig.DEFAULT_USERS
    override val callLogging: Boolean = BasicConfig.DEFAULT_CALL_LOGGING
    override val ignoreUploadDownloadIOException: Boolean = BasicConfig.DEFAULT_IGNORE_UPLOAD_DOWNLOAD_IO_EXCEPTION
    private val config: File? by option(help = "Config json (Overwrite all params)").file(mustExist = true, canBeDir = false, mustBeReadable = true)

    init {
        context {
            helpFormatter = CliktHelpFormatter(showDefaultValues = true)
        }
    }

    override fun run() {
        config.let {
            if (it == null) {
                launchServer(this)
            } else {
                launchServer(ConfigManager.loadConfigFromJsonFile(it))
            }
        }
    }
}

private class DefaultConfig : CliktCommand(help = "Generate default config") {
    private val path: File by option(help = "Default config output path").file(mustExist = true, canBeDir = true, canBeFile = false).defaultLazy {
        File(ConfigManager.DEFAULT_CONFIG_PATH)
    }

    override fun run() {
        val file = ConfigManager.generateDefaultJsonConfigFile(path)
        println("Default config json has been generated in: ${file.absolutePath.replace("${File.separator}.${File.separator}", File.separator)}")
    }
}