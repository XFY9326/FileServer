package tool.xfy9326.fileserver.utils

import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.coroutineScope
import tool.xfy9326.fileserver.beans.IConfig
import java.io.File

class FileManager constructor(config: IConfig) {
    private val fileRoot = File(config.root)

    init {
        if (fileRoot.isFile) {
            error("Root path must be directory! Path: ${config.root}")
        } else if (!fileRoot.exists() && !fileRoot.mkdirs()) {
            error("Directory create failed! Path: ${config.root}")
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

    fun listFiles(path: String): List<String> {
        val targetFile = File(fileRoot, path)
        if (targetFile.isDirectory) {
            return targetFile.listFiles { file ->
                !file.name.startsWith(".")
            }?.map {
                if (it.isDirectory) {
                    it.name + File.separator
                } else {
                    it.name
                }
            } ?: emptyList()
        } else if (targetFile.isFile) {
            error("Current path is a file, not directory! Path: $path")
        } else {
            throw NoSuchFileException(targetFile, reason = "File not exists! Path: $path")
        }
    }

    fun hasFile(path: String) = File(fileRoot, path).isFile

    suspend fun readFile(path: String, output: ByteWriteChannel) = coroutineScope {
        val targetFile = File(fileRoot, path)
        if (targetFile.name.startsWith(".")) {
            error("Not allowed to download invisible file! File: ${targetFile.name}")
        }
        if (!targetFile.isFile || !targetFile.canRead()) {
            throw FileSystemException(targetFile, reason = "File is s directory or not readable! Path: $path")
        }
        targetFile.readChannel(coroutineContext = coroutineContext).copyAndClose(output)
    }

    suspend fun saveFile(path: String, input: ByteReadChannel) = coroutineScope {
        val targetFile = File(fileRoot, path)
        if (targetFile.name.startsWith(".")) {
            error("Not allowed to upload invisible file! File: ${targetFile.name}")
        }
        if (targetFile.exists()) {
            if (targetFile.isFile) {
                throw FileAlreadyExistsException(targetFile, reason = "File already exists! Path: $path")
            } else {
                throw FileAlreadyExistsException(targetFile, reason = "Same name directory already exists! Path: $path")
            }
        }
        targetFile.parentFile.let {
            if (it == null || !it.exists() && !it.mkdirs()) {
                throw FileSystemException(it, reason = "Parent directory create failed! Path: $path")
            } else if (it.isFile) {
                throw FileAlreadyExistsException(it, reason = "Parent directory is a file! Path: $path")
            }
        }
        targetFile.writeChannel(coroutineContext).use {
            input.copyAndClose(this)
        }
    }
}