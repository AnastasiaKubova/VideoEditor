package com.example.videoeditor.util

import android.content.Intent
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import com.example.videoeditor.R

object FilePicker {

    const val PICK_AUDIO = 3
    private const val SELECT_TYPE = "audio/*"

    fun loadAudioFromGallery(fragment: Fragment) {

        /* Get from files folder. */
        val getIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        getIntent.addCategory(Intent.CATEGORY_OPENABLE)
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