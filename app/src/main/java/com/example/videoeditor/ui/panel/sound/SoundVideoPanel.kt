package com.example.videoeditor.ui.panel.sound

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import com.example.videoeditor.MainActivity
import com.example.videoeditor.R
import com.example.videoeditor.ui.BaseFragment

class SoundVideoPanel: BaseFragment(), MainActivity.BackPressListener,
    BaseFragment.ConfirmationDialogListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.sounds_video_panel, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* Init menu. */
        changeOptionMenuPanel(true, resources.getString(R.string.edit_sound))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_video -> {
                showConfirmationDialog()
                true
            }
            R.id.pickup_file_video -> {
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
        MainActivity.backPressListener = this
        confirmationDialogListener = this
    }

    override fun onPause() {
        super.onPause()
        MainActivity.backPressListener = null
        confirmationDialogListener = null
    }

    override fun onBackPressed() {
        findNavController().popBackStack()
        findNavController().navigate(R.id.mainVideoPanel)
    }

    override fun onPositiveButtonClick() {
        onBackPressed()
    }

    override fun onNegativeButtonClick() {
        onBackPressed()
    }
}