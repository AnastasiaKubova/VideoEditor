package com.example.videoeditor.ui.fragment.picker.audio

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.videoeditor.model.FileData
import com.example.videoeditor.util.FilePicker
import com.example.videoeditor.util.Preference
import java.net.URI

class AudioPickerViewModel: ViewModel() {

    val audioList: MutableLiveData<MutableList<FileData>> by lazy {
        MutableLiveData<MutableList<FileData>>()
    }

    val currentFileName: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun saveFolder() {
        Preference.setSourceFolder(FilePicker.currentFolderPath.path)
    }

    fun updateFileList() {
        audioList.value = FilePicker.getLastFolderList()
        updateCurrentFolderName()
    }

    fun updateListForParentFolder() {
        audioList.value = FilePicker.getParentFiles()
        updateCurrentFolderName()
    }

    fun updateListForSelectFolder(file: FileData) {
        audioList.value = FilePicker.getFromSelectFolderList(URI.create(file.file!!.absolutePath))
        updateCurrentFolderName()
    }

    private fun updateCurrentFolderName() {
        currentFileName.value = FilePicker.getFileName()
    }
}