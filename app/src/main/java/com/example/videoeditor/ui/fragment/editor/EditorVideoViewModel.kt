package com.example.videoeditor.ui.fragment.editor

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

// https://developer.android.com/reference/android/media/MediaCodec
class EditorVideoViewModel : ViewModel() {

    private var player: SimpleExoPlayer? = null

    val videoUri: MutableLiveData<Uri> by lazy {
        MutableLiveData<Uri>()
    }

    val isPlay: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun prepareVideoPlayer(context: Context) : SimpleExoPlayer? {
        if (player != null) {
            player?.release()
            player = null
        }
        player = SimpleExoPlayer.Builder(context).build()
        player?.let {
            val mediaItem = MediaItem.fromUri(videoUri.value!!)
            it.setMediaItem(mediaItem)
            it.prepare()
            it.repeatMode = Player.REPEAT_MODE_ALL
            it.play()
            isPlay.value = it.isPlaying
        }
        return player
    }

    fun pauseOrPlayVideo(play: Boolean) {
        player?.let {
            if (play) {
                it.play()
            } else {
                it.pause()
            }
            isPlay.value = play
        }
    }
}