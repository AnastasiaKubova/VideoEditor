package com.example.videoeditor.model

import com.example.videoeditor.enum.FileType
import java.io.File

data class FileData(val fileType: FileType, val file: File?) {
}