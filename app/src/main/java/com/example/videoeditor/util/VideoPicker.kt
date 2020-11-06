package com.example.videoeditor.util

import android.content.Intent
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import com.example.videoeditor.R

object VideoPicker {

    const val REQUEST_VIDEO_CAPTURE = 1
    const val PICK_VIDEO = 2
    private const val SELECT_TYPE = "video/*"

    fun startRecodeVideoViaCamera(fragment: Fragment) {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(fragment.requireContext().packageManager)?.also {
                fragment.startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
            }
        }
    }

    fun loadVideoFromGallery(fragment: Fragment) {

        /* Get from files folder. */
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = SELECT_TYPE

        /* Get from photo. */
        val pickIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickIntent.type = SELECT_TYPE

        /* Start activity. */
        val chooserIntent = Intent.createChooser(getIntent, fragment.getString(R.string.select_video))
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
        fragment.startActivityForResult(chooserIntent, PICK_VIDEO)
    }
}