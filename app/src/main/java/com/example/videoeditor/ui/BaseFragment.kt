package com.example.videoeditor.ui

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.videoeditor.R

open class BaseFragment: Fragment() {

    val WRITE_REQUEST_CODE = 1
    val READ_REQUEST_CODE = 2
    companion object {
        var mCurrentVideo: Uri? = null
        var changeMenuListener: ChangeMenuListener? = null
        var confirmationDialogListener: ConfirmationDialogListener? = null
        var changeVideoListener: ChangeVideoListener? = null
        var videoEditorChangeListener: VideoEditorChangeListener? = null
        var permissionsChangeListener: PermissionsChangeListener? = null
    }

    fun processRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_REQUEST_CODE -> if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                permissionsChangeListener?.writeRequest()
            }
            READ_REQUEST_CODE -> {
                permissionsChangeListener?.readRequest()
            }
        }
    }

    fun changeOptionMenuPanel(isVisible: Boolean, title: String) {
        setHasOptionsMenu(true)
        changeMenuListener?.onBackPressVisibleChange(isVisible, title)
    }

    fun showConfirmationDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Confirmation dialog")
        dialog.setMessage("Are you sure that you want to save this changes?")
        dialog.setPositiveButton("Yes") { dialog, value ->
            confirmationDialogListener?.onPositiveButtonClick()
            dialog.cancel()
        }
        dialog.setNegativeButton("No") { dialog, value ->
            confirmationDialogListener?.onPositiveButtonClick()
            dialog.cancel()
        }
        dialog.show()
    }

    interface ChangeMenuListener {
        fun onBackPressVisibleChange(isVisible: Boolean, title: String)
    }

    interface ChangeVideoListener {
        fun onSelectVideo(uri: Uri)
    }

    interface ConfirmationDialogListener {
        fun onPositiveButtonClick()
        fun onNegativeButtonClick()
    }

    interface PermissionsChangeListener {
        fun readRequest()
        fun writeRequest()
    }

    interface VideoEditorChangeListener {
        fun selectedAudio(audioPath: String?)
        fun selectedInterval(start: Double, end: Double)
        fun selectedText(text: String)
    }
}