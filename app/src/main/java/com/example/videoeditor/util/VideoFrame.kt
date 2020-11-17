package com.example.videoeditor.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import java.io.File

// https://developer.android.com/training/data-storage/shared/media
object VideoFrame {

    val LOG_TAG = "VideoFrame"

    fun getVideoFrames(context: Context, file: Uri, countFrames: Int): ArrayList<Bitmap> {
        val retriever = MediaMetadataRetriever()
        val bitmaps: ArrayList<Bitmap> = arrayListOf()
        try {
            retriever.setDataSource(context, file)
            var duration = 0
            var offsetFrame = 0L
            if (!TextUtils.isEmpty(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))) {
                duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toInt()
            }
            if (duration != null) {
                offsetFrame = (duration / countFrames).toLong()
            }
            for (i in 0 until countFrames) {
                val bitmap = retriever.getFrameAtTime(offsetFrame, MediaMetadataRetriever.OPTION_CLOSEST)
                if (bitmap != null) {
                    bitmaps.add(bitmap)
                }
            }
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
            Log.d(LOG_TAG, "Something went wrong during getting video frames. Error - IllegalArgumentException: ${ex.message}")
        } catch (ex: RuntimeException) {
            Log.d(LOG_TAG, "Something went wrong during getting video frames. Error - RuntimeException: ${ex.message}")
            ex.printStackTrace()
        } finally {
            try {
                retriever.release()
            } catch (ex: RuntimeException) {
            }
        }
        return bitmaps
    }
}