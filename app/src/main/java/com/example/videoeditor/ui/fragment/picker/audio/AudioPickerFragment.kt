package com.example.videoeditor.ui.fragment.picker.audio

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videoeditor.R
import com.example.videoeditor.model.FileData
import com.example.videoeditor.ui.BaseFragment

class AudioPickerFragment: BaseFragment(), AudioPickerHolder.AudioPickerItemListener,
    BaseFragment.ConfirmationDialogListener {

    private lateinit var audioPickerRecyclerView: RecyclerView
    private lateinit var audioPickerAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val viewModel by viewModels<AudioPickerViewModel>()

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
        return inflater.inflate(R.layout.audio_picker_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initObservers()

        /* Init menu. */
        changeOptionMenuPanel(true, resources.getString(R.string.select_audio))
        initList()
        viewModel.updateFileList()
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

    override fun onAudioPickerSelected(item: FileData) {

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
        val audioList = Observer<MutableList<FileData>> { list ->
            updateAdapter(list)
        }
        viewModel.audioList.observe(requireActivity(), audioList)
    }

    private fun updateAdapter(list: MutableList<FileData>) {
        (audioPickerAdapter as AudioPickerAdapter).items = list
        audioPickerAdapter.notifyDataSetChanged()
    }

    private fun initList() {
        viewManager = LinearLayoutManager(activity)
        audioPickerAdapter = AudioPickerAdapter(mutableListOf(), this)
        audioPickerRecyclerView = requireActivity().findViewById<RecyclerView>(R.id.audio_play_list).apply {
            layoutManager = viewManager
            adapter = audioPickerAdapter
        }
    }
}