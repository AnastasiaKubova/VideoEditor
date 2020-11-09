package com.example.videoeditor.util

import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.example.videoeditor.R
import com.example.videoeditor.enum.FileType
import com.example.videoeditor.model.FileData
import com.example.videoeditor.util.Constants.sAudioExtension
import java.io.File
import java.net.URI

object FilePicker {

    const val PICK_AUDIO = 3
    private const val SELECT_TYPE = "audio/*"

    fun loadAudioFromGallery(fragment: Fragment) {

        /* Get from files folder. */
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = SELECT_TYPE

        /* Get from photo. */
        val pickIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        )
        pickIntent.type = SELECT_TYPE

        /* Start activity. */
        val chooserIntent = Intent.createChooser(getIntent, fragment.getString(R.string.select_audio))
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
        fragment.startActivityForResult(chooserIntent, PICK_AUDIO)
    }
}