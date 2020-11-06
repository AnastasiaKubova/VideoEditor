package com.example.videoeditor.ui.panel.effect

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.example.videoeditor.R
import com.example.videoeditor.ui.BaseFragment
import kotlinx.android.synthetic.main.effect_video_panel.*

class EffectVideoPanel: BaseFragment(), BaseFragment.ConfirmationDialogListener {

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
        return inflater.inflate(R.layout.effect_video_panel, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* Init menu. */
        changeOptionMenuPanel(true, resources.getString(R.string.edit_content))
        add_text_effect.setOnClickListener {

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

    private fun onBackPressed() {
        findNavController().popBackStack()
        findNavController().navigate(R.id.mainVideoPanel)
    }
}