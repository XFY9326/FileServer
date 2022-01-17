package tool.xfy9326.fileserver.beans

interface IConfig {
    val host: String
    val port: Int
    val root: String
    val allowAnonymous: Boolean
    val allowAnonymousDownload: Boolean
    val users: Map<String, String>
    val callLogging: Boolean
    val ignoreUploadDownloadIOException: Boolean
}