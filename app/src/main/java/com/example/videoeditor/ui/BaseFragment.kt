package com.example.videoeditor.ui

import android.app.AlertDialog
import android.net.Uri
import androidx.fragment.app.Fragment

open class BaseFragment: Fragment() {
    
    companion object {
        var changeMenuListener: ChangeMenuListener? = null
        var confirmationDialogListener: ConfirmationDialogListener? = null
        var changeVideoListener: ChangeVideoListener? = null
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
}