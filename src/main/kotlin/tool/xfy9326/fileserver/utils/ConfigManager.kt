package tool.xfy9326.fileserver.utils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tool.xfy9326.fileserver.beans.BasicConfig
import tool.xfy9326.fileserver.beans.IConfig
import java.io.File

object ConfigManager {
    private const val DEFAULT_CONFIG_NAME = "config.default.json"
    private val ConfigJson = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = true
    }
    const val DEFAULT_CONFIG_PATH = "."

    fun loadConfigFromJsonFile(file: File): IConfig =
        ConfigJson.decodeFromString<BasicConfig>(file.readText())

    fun generateDefaultJsonConfigFile(file: File): File =
        File(file, DEFAULT_CONFIG_NAME).also {
            it.writeText(ConfigJson.encodeToString(BasicConfig()))
        }
}