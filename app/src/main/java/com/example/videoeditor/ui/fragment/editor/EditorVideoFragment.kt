package com.example.videoeditor.ui.fragment.editor

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.videoeditor.R
import com.example.videoeditor.ui.BaseFragment
import com.example.videoeditor.ui.panel.sound.SoundVideoPanel
import kotlinx.android.synthetic.main.editor_fragment.*
import kotlinx.android.synthetic.main.video_controls.*

class EditorVideoFragment: BaseFragment(),
    BaseFragment.ChangeVideoListener {

    private val viewModel by viewModels<EditorVideoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.editor_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initObservers()

        /* Init listeners. */
        exo_play.setOnClickListener {
            viewModel.pauseOrPlayVideo(true)
        }
        exo_pause.setOnClickListener {
            viewModel.pauseOrPlayVideo(false)
        }
    }

    override fun onSelectVideo(uri: Uri) {
        viewModel.videoUri.value = uri
    }

    override fun onResume() {
        super.onResume()
        changeVideoListener = this
        viewModel.pauseOrPlayVideo(true)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseOrPlayVideo(false)
    }

    private fun initObservers() {
        val uriVideo = Observer<Uri> { uri ->

            // Alternative view - http://developer.alexanderklimov.ru/android/views/videoview.php.
            video_view.player = viewModel.prepareVideoPlayer(requireContext())
        }
        val isPlay = Observer<Boolean> { isPlay ->
            exo_pause.visibility = if (isPlay) View.VISIBLE else View.GONE
            exo_play.visibility = if (isPlay) View.GONE else View.VISIBLE
        }
        viewModel.videoUri.observe(viewLifecycleOwner, uriVideo)
        viewModel.isPlay.observe(viewLifecycleOwner, isPlay)
    }
}