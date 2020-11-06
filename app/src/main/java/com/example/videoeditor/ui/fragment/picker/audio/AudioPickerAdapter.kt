package com.example.videoeditor.ui.fragment.picker.audio

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videoeditor.R
import com.example.videoeditor.model.FileData

class AudioPickerAdapter(var items: MutableList<FileData>, var listener: AudioPickerHolder.AudioPickerItemListener?): RecyclerView.Adapter<AudioPickerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioPickerHolder {
        return AudioPickerHolder(View.inflate(parent.context, R.layout.audio_item, null), listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: AudioPickerHolder, position: Int) {
        holder.bind(items[position])
    }
}