package com.example.videoeditor.ui.panel.cut

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.example.videoeditor.R
import com.example.videoeditor.ui.BaseFragment
import com.example.videoeditor.util.VideoFrame
import kotlinx.android.synthetic.main.cut_video_panel.*
import java.io.File
import java.net.URI

class CutVideoPanel: BaseFragment(), BaseFragment.ConfirmationDialogListener {

    private val COUNT_FRAMES = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback {
            onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.cut_video_panel, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* Init menu. */
        changeOptionMenuPanel(true, resources.getString(R.string.clip_video))
        initClipSeekbar()
        seekbar_video_clip.setOnTouchListener { v, event ->
            seekbar_video_clip.onTouch(v, event)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.edit_video -> {
                showConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onResume() {
        super.onResume()
        confirmationDialogListener = this
    }

    override fun onPause() {
        super.onPause()
        confirmationDialogListener = null
    }

    override fun onPositiveButtonClick() {
        onBackPressed()
    }

    override fun onNegativeButtonClick() {
        onBackPressed()
    }

    private fun initClipSeekbar() {
        if (mCurrentVideo != null) {
            seekbar_video_clip.setFrames(VideoFrame.getVideoFrames(requireActivity(), mCurrentVideo!!, COUNT_FRAMES))
        }
    }

    private fun onBackPressed() {
        findNavController().popBackStack()
        findNavController().navigate(R.id.mainVideoPanel)
    }
}