package com.example.videoeditor.ui.panel.sound

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.videoeditor.R
import com.example.videoeditor.ui.BaseFragment
import com.example.videoeditor.util.FilePicker
import com.example.videoeditor.util.VideoPicker
import kotlinx.android.synthetic.main.sounds_video_panel.*
import java.io.File

class SoundVideoPanel: BaseFragment(), BaseFragment.ConfirmationDialogListener {

    private val viewModel by viewModels<SoundVideoViewModel>()

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
        return inflater.inflate(R.layout.sounds_video_panel, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initObservers()

        /* Init menu. */
        changeOptionMenuPanel(true, resources.getString(R.string.edit_sound))
        prepareSeekBarForSelectedTrack(false)
        clear_sound.setOnClickListener {
            viewModel.selectedAudio.value = null
            prepareSeekBarForSelectedTrack(false)
        }
        seekbar_sound_selected.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        seekbar_sound_original.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
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
            R.id.pickup_file_video -> {
                FilePicker.loadAudioFromGallery(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FilePicker.PICK_AUDIO && resultCode == AppCompatActivity.RESULT_OK) {
            data?.data?.let {
                viewModel.selectedAudio.value = it
                prepareSeekBarForSelectedTrack(true)
                Toast.makeText(requireContext(), getString(R.string.selected_audio_track), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.audio_change_menu, menu)
    }

    override fun onResume() {
        super.onResume()
        confirmationDialogListener = this
    }

    override fun onPause() {
        super.onPause()
        confirmationDialogListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.selectedAudio.value = null
    }

    override fun onPositiveButtonClick() {
        onBackPressed()
    }

    override fun onNegativeButtonClick() {
        onBackPressed()
    }

    private fun prepareSeekBarForSelectedTrack(isEnabled: Boolean) {
        seekbar_sound_selected.progress = 0
        seekbar_sound_selected.isEnabled = isEnabled
    }

    private fun onBackPressed() {
        findNavController().popBackStack()
        findNavController().navigate(R.id.mainVideoPanel)
    }

    private fun initObservers() {
        val selectedAudio = Observer<Uri> { uri ->
            if (uri != null) {
                val file = File(uri.path)
                current_selected_track.text = file.name
            } else {
                current_selected_track.text = ""
            }
        }
        viewModel.selectedAudio.observe(viewLifecycleOwner, selectedAudio)
    }
}