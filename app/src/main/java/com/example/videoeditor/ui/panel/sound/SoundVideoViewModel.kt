package com.example.videoeditor.ui.panel.sound

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SoundVideoViewModel: ViewModel() {

    val selectedAudio: MutableLiveData<Uri> by lazy {
        MutableLiveData<Uri>()
    }
}