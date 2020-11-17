package com.example.videoeditor.service

import android.content.Context
import android.content.Intent
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaMuxer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.core.app.JobIntentService
import com.example.videoeditor.util.Constants
import java.io.File
import java.io.FileDescriptor
import java.nio.ByteBuffer

class VideoProcessingService: JobIntentService() {

    val LOG_TAG = "VideoProcessingService"

    companion object {
        val videoProcessingListener: VideoProcessingServiceListener? = null
        val MUXER_VIDEO = "VideoProcessingService.MUXER_VIDEO"
        val JOB_ID = 123

        fun enqueueWork(context: Context?, bundle: Bundle?, action: String) {
            if (context == null) {
                return
            }
            val intent = Intent(context, VideoProcessingService::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            intent.action = action
            enqueueWork(context, VideoProcessingService::class.java, JOB_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        when (intent.action) {
            MUXER_VIDEO -> {
                val videoPath = intent.getStringExtra(Constants.VIDEO_PATH)
                val audioPath = intent.getStringExtra(Constants.AUDIO_PATH)
                if (videoPath != null && audioPath != null) {
                    val path = createVideoWithNewAudio(Uri.parse(videoPath), Uri.parse(audioPath))
                    Log.d(LOG_TAG, path)
                } else {
                    Log.d(LOG_TAG, "Something went wrong during creating video")
                }
            }
        }
    }

    private fun createVideoWithNewAudio(videoPath: Uri, audioFilePath: Uri): String {

        // Prepare output file.
        val videoName = System.currentTimeMillis()
        val file = File(Environment.getExternalStorageDirectory(), "$videoName.mp4")
        file.createNewFile()
        val outputFile = file.absolutePath
        var videoMuxerResult = false
        var audioMuxerResult = false
        try {

            // Prepare video.
            val videoExtractor = MediaExtractor()
            videoExtractor.setDataSource(applicationContext, videoPath, null)

            // Prepare audio.
            val audioExtractor = MediaExtractor()
            audioExtractor.setDataSource(applicationContext, audioFilePath, null)

            // Prepare new video muxer.
            val newVideoMuxer = MediaMuxer(outputFile, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

            // Get video data from video file and set to new video.
            videoExtractor.selectTrack(0)
            val videoFormat = videoExtractor.getTrackFormat(0)
            val videoTrack = newVideoMuxer.addTrack(videoFormat)

            // Get audio data from audio file and set to new video.
            audioExtractor.selectTrack(0)
            val audioFormat = audioExtractor.getTrackFormat(0)
            val audioTrack = newVideoMuxer.addTrack(audioFormat)

            // Set seek value.
            videoExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
            audioExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)

            newVideoMuxer.start()
            videoMuxerResult = writeSampleData(newVideoMuxer, videoExtractor, videoTrack)
            audioMuxerResult = writeSampleData(newVideoMuxer, audioExtractor, audioTrack)
            if (!videoMuxerResult || !audioMuxerResult) {
                videoProcessingListener?.filedProcessingVideo()
            } else {
                videoProcessingListener?.completeProcessingVideo()
            }
            newVideoMuxer.stop()
            newVideoMuxer.release()

        } catch (ex: java.lang.Exception) {
            Log.d(LOG_TAG, "Something went wrong during creating video. " + ex.message)
        }
        return if (videoMuxerResult && audioMuxerResult) file.absolutePath else ""
    }

    /**
     *
     * Create new video.
     *
     * This method was created based on this source: https://sisik.eu/blog/android/media/mix-audio-into-video
     *
     * @param muxer - mixer for new video. Note, that this value passed bu link and will be changed as in this method and when this method is called.
     * @param sourceExtractor - media extractor.
     * @param sourceTrack - number of track in new video.
     *
     * @return true - if processing complete success.
     */
    private fun writeSampleData(muxer: MediaMuxer, sourceExtractor: MediaExtractor, sourceTrack: Int): Boolean {
        val sampleSize = 256 * 1024
        val sourceBuffer: ByteBuffer = ByteBuffer.allocate(sampleSize)
        val sourceBufferInfo = MediaCodec.BufferInfo()
        try {
            var sawEOS = false
            var frameCount = 0
            val offset = 100
            while (!sawEOS) {
                sourceBufferInfo.offset = offset
                sourceBufferInfo.size = sourceExtractor.readSampleData(sourceBuffer, offset)
                if (sourceBufferInfo.size < 0) {
                    sawEOS = true
                    sourceBufferInfo.size = 0
                } else {
                    sourceBufferInfo.presentationTimeUs = sourceExtractor.sampleTime
                    sourceBufferInfo.flags = sourceExtractor.sampleFlags
                    muxer.writeSampleData(sourceTrack, sourceBuffer, sourceBufferInfo)
                    sourceExtractor.advance()
                    frameCount++
                }
            }
        } catch (e: Exception) {
            Log.d(LOG_TAG, e.message.toString())
            return false
        }
        return true
    }

    interface VideoProcessingServiceListener {
        fun completeProcessingVideo()
        fun filedProcessingVideo()
    }
}