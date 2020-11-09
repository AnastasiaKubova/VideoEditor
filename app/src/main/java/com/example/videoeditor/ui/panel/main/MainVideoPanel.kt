package com.example.videoeditor.ui.panel.main

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.videoeditor.R
import com.example.videoeditor.ui.BaseFragment
import com.example.videoeditor.util.VideoPicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.main_video_panel.*

class MainVideoPanel: BaseFragment() {

    private lateinit var dialog: BottomSheetDialog
    private lateinit var dialogView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_video_panel, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* Init menu. */
        changeOptionMenuPanel(false, resources.getString(R.string.edit_video))

        /* Init UI elements. */
        initBottomSheet()
        initBottomMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_video -> {
                showBottomSheetDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.settings_menu, menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VideoPicker.REQUEST_VIDEO_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            data?.data?.let {
                changeVideoListener?.onSelectVideo(it)
            }
        } else if (requestCode == VideoPicker.PICK_VIDEO && resultCode == AppCompatActivity.RESULT_OK) {
            data?.data?.let {
                changeVideoListener?.onSelectVideo(it)
            }
        }
    }

    private fun initBottomMenu() {
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.clip_video -> {
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.cutVideoPanel)
                    true
                }
                R.id.edit_sound -> {
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.soundVideoPanel)
                    true
                }
                R.id.edit_content -> {
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.effectVideoPanel)
                    true
                }
                else -> false
            }
        }
    }

    private fun initBottomSheet() {

        /* Init bottom sheet dialog. */
        dialogView = layoutInflater.inflate(R.layout.menu_bottom_sheet, null)
        dialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogView)
    }

    private fun showBottomSheetDialog() {
        dialogView.findViewById<TextView>(R.id.select_video).setOnClickListener {
            VideoPicker.loadVideoFromGallery(this)
            dialog.cancel()
        }
        dialogView.findViewById<TextView>(R.id.create_video).setOnClickListener {
            VideoPicker.startRecodeVideoViaCamera(this)
            dialog.cancel()
        }
        dialogView.findViewById<TextView>(R.id.save_video).setOnClickListener {
            dialog.cancel()
        }
        dialog.show()
    }
}