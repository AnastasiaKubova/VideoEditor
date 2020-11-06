package com.example.videoeditor.ui.fragment.picker.audio

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.videoeditor.model.FileData

class AudioPickerHolder(itemView: View, var listener: AudioPickerItemListener?) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: FileData) {

        itemView.setOnClickListener { listener?.onAudioPickerSelected(item) }
    }

    interface AudioPickerItemListener {
        fun onAudioPickerSelected(item: FileData)
    }
}