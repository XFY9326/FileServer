package tool.xfy9326.fileserver.utils

import io.ktor.util.cio.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import tool.xfy9326.fileserver.beans.IConfig
import java.io.File
import java.io.InputStream
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.isHidden

class FileManager(config: IConfig) {
    private val fileRootPath = Path(config.root).normalize()
    private val fileRoot = fileRootPath.toFile()

    init {
        if (fileRoot.isFile) {
            error("${config.root}: Root path must be directory!")
        } else if (!fileRoot.exists() && !fileRoot.mkdirs()) {
            error("${config.root}: Directory create failed!")
        }
    }

    companion object {
        fun List<String>?.joinToPath(): String {
            return if (isNullOrEmpty()) {
                ""
            } else {
                joinToString(File.separator)
            }
        }
    }

    private fun getTargetFile(path: String): File {
        val targetPath = Path(fileRootPath.toString(), path).normalize()
        if (targetPath.startsWith(fileRootPath)) {
            for (i in fileRootPath.nameCount until targetPath.nameCount) {
                if (targetPath.getName(i).exists() && targetPath.getName(i).isHidden()) {
                    if (i == targetPath.nameCount - 1) {
                        throw AccessDeniedException(File(path), reason = "Not allow accessing hidden file!")
                    } else {
                        throw AccessDeniedException(File(path), reason = "Not allow accessing hidden directory!")
                    }
                }
            }
            return targetPath.toFile()
        } else {
            throw AccessDeniedException(File(path), reason = "Not allow accessing file that not under the root!")
        }
    }

    fun listFiles(path: String, fileOnly: Boolean = false): List<String> {
        val targetFile = getTargetFile(path)
        if (targetFile.isDirectory) {
            return targetFile.listFiles { file ->
                !file.isHidden && (!fileOnly || file.isFile)
            }?.map {
                if (it.isDirectory) {
                    it.name + File.separator
                } else {
                    it.name
                }
            } ?: emptyList()
        } else if (targetFile.isFile) {
            error("$path: This path is a file, not directory!")
        } else {
            throw NoSuchFileException(File(path), reason = "File not exists!")
        }
    }

    fun hasFile(path: String) = File(fileRoot, path).isFile

    fun getFile(path: String): File {
        val targetFile = getTargetFile(path)
        if (!targetFile.isFile || !targetFile.canRead()) {
            throw FileSystemException(File(path), reason = "File is s directory or not readable!")
        }
        return targetFile
    }

    suspend fun saveFile(path: String, input: InputStream) = saveFile(path) {
        it.writeChannel(coroutineContext).use {
            input.copyTo(this)
        }
    }

    suspend fun saveFile(path: String, input: ByteReadChannel) = saveFile(path) {
        it.writeChannel(coroutineContext).use {
            input.copyAndClose(this)
        }
    }

    private suspend fun saveFile(path: String, block: suspend CoroutineScope.(File) -> Unit) = coroutineScope {
        val targetFile = getTargetFile(path)
        if (targetFile.exists()) {
            if (targetFile.isFile) {
                throw FileAlreadyExistsException(File(path), reason = "File already exists!")
            } else {
                throw FileAlreadyExistsException(File(path), reason = "Directory already exists!")
            }
        }
        targetFile.parentFile.let {
            if (it == null || !it.exists() && !it.mkdirs()) {
                throw FileSystemException(File(path), reason = "Parent directory create failed!")
            } else if (it.isFile) {
                throw FileAlreadyExistsException(File(path), reason = "Parent directory is a file!")
            }
        }
        block(targetFile)
    }
}