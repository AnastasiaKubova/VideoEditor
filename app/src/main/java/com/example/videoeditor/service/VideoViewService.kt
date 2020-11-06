package com.example.videoeditor.service

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.view.Surface
import java.io.File
import java.nio.ByteBuffer

class VideoViewService: MediaCodec.Callback() {

    private var mMediaExtractor: MediaExtractor = MediaExtractor()
    private var mDecoder: MediaCodec? = null

    fun prepareVideoView(file: File, surface: Surface) {
        mMediaExtractor.setDataSource(file.absolutePath)
        var mediaFormat: MediaFormat? = null
        var mimeType = ""
        for (i in 0 until mMediaExtractor.trackCount) {
            mediaFormat = mMediaExtractor.getTrackFormat(i)
            val mime: String = mediaFormat.getString(MediaFormat.KEY_MIME)!!
            if (mime.startsWith("video/")) {
                mMediaExtractor.selectTrack(i)
                mimeType = mime
                break
            }
        }
        mDecoder = MediaCodec.createDecoderByType(mimeType)
        mDecoder!!.setCallback(this)
        mDecoder!!.configure(mediaFormat!!, surface, null, 0)
    }

    fun startVideoView() {
        mDecoder!!.start() // запускаем кодер
    }

    override fun onOutputBufferAvailable(
        codec: MediaCodec,
        outputBufferId: Int,
        info: MediaCodec.BufferInfo
    ) {
        val render = info.size > 0
        codec.releaseOutputBuffer(outputBufferId, render)
    }

    override fun onInputBufferAvailable(codec: MediaCodec, inputBufferId: Int) {
        val inputBuffer: ByteBuffer? = codec.getInputBuffer(inputBufferId)
        inputBuffer?.clear()
        val sampleSize = mMediaExtractor.readSampleData(inputBuffer!!, 0)

        if (sampleSize >= 0) {
            codec.queueInputBuffer(inputBufferId, 0, sampleSize, mMediaExtractor.sampleTime, 0)
            mMediaExtractor.advance()
        } else {
            codec.queueInputBuffer(inputBufferId, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
        }
    }

    override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {
    }

    override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
    }
}