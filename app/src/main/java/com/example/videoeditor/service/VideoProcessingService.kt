package com.example.videoeditor.service

import android.content.Intent
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaMuxer
import android.os.Environment
import android.util.Log
import androidx.core.app.JobIntentService
import java.io.File
import java.nio.ByteBuffer

class VideoProcessingService: JobIntentService() {

    override fun onHandleWork(intent: Intent) {

    }

    private fun createVideoWithNewAudio(videoPath: String, audioFilePath: String) {

        // Prepare output file.
        val file = File(Environment.getExternalStorageDirectory(), "final2.mp4")
        file.createNewFile()
        val outputFile = file.absolutePath

        // Prepare video.
        val videoExtractor = MediaExtractor()
        videoExtractor.setDataSource(videoPath)

        // Prepare audio.
        val audioExtractor = MediaExtractor()
        audioExtractor.setDataSource(audioFilePath)

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
        writeSampleData(newVideoMuxer,videoExtractor, videoTrack)
        writeSampleData(newVideoMuxer, audioExtractor, audioTrack)
        newVideoMuxer.stop()
        newVideoMuxer.release()
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
     */
    private fun writeSampleData(muxer: MediaMuxer, sourceExtractor: MediaExtractor, sourceTrack: Int) {
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
            Log.d(javaClass::getName.toString(), e.message.toString())
        }
    }
}