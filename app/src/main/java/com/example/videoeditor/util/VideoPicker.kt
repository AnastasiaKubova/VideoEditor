package com.example.videoeditor.util

import android.content.Intent
import android.provider.MediaStore
import androidx.fragment.app.Fragment

object VideoPicker {

    const val REQUEST_VIDEO_CAPTURE = 1

    fun startRecodeVideoViaCamera(fragment: Fragment) {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(fragment.requireContext().packageManager)?.also {
                fragment.startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
            }
        }
    }
}