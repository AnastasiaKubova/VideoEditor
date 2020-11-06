package com.example.videoeditor.ui.panel.sound

import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.videoeditor.R
import com.example.videoeditor.ui.BaseFragment
import kotlinx.android.synthetic.main.sounds_video_panel.*

class SoundVideoPanel: BaseFragment(), BaseFragment.ConfirmationDialogListener {

    private val viewModel by viewModels<SoundVideoViewModel>()
    companion object {
        var openEditPickerListener: OpenAudioPickerListener? = null
    }

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
        clear_sound.setOnClickListener {
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
                openEditPickerListener?.onOpenAudioPickerListener()
                true
            }
            else -> super.onOptionsItemSelected(item)
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

    override fun onPositiveButtonClick() {
        onBackPressed()
    }

    override fun onNegativeButtonClick() {
        onBackPressed()
    }

    private fun onBackPressed() {
        findNavController().popBackStack()
        findNavController().navigate(R.id.mainVideoPanel)
    }

    private fun initObservers() {
        val titleAudio = Observer<String> { title ->
            current_selected_track.text = title
        }
        viewModel.titleAudio.observe(viewLifecycleOwner, titleAudio)
    }

    interface OpenAudioPickerListener {
        fun onOpenAudioPickerListener()
    }
}