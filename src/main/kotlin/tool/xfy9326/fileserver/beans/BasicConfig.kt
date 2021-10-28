package tool.xfy9326.fileserver.beans

import kotlinx.serialization.Serializable

@Serializable
data class BasicConfig(
    override val host: String = DEFAULT_HOST,
    override val port: Int = DEFAULT_PORT,
    override val root: String = DEFAULT_ROOT,
    override val allowAnonymous: Boolean = DEFAULT_ALLOW_ANONYMOUS,
    override val allowAnonymousDownload: Boolean = DEFAULT_ALLOW_ANONYMOUS_DOWNLOAD,
    override val users: Map<String, String> = DEFAULT_USERS
) : IConfig {
    companion object {
        const val DEFAULT_HOST = "0.0.0.0"
        const val DEFAULT_PORT = 8080
        const val DEFAULT_ROOT = "files"
        const val DEFAULT_ALLOW_ANONYMOUS = true
        const val DEFAULT_ALLOW_ANONYMOUS_DOWNLOAD = true
        val DEFAULT_USERS = emptyMap<String, String>()
    }
}