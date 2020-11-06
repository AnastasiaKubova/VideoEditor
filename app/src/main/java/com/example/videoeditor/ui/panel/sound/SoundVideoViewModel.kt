package com.example.videoeditor.ui.panel.sound

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SoundVideoViewModel: ViewModel() {

    val titleAudio: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}