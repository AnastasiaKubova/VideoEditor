package com.example.videoeditor

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.videoeditor.service.VideoProcessingService
import com.example.videoeditor.ui.BaseFragment
import com.example.videoeditor.util.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.video_controls.*

class MainActivity : AppCompatActivity(), BaseFragment.ChangeMenuListener {

    companion object {
        var backPressListener: BackPressListener? = null
    }

    private var navHostMainFragment: NavHostFragment? = null
    private var navMainController: NavController? = null

    private var navHostPanelFragment: NavHostFragment? = null
    private var navPanelController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Init fragments. */
        navHostMainFragment = supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment
        navMainController = navHostMainFragment!!.navController
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        BaseFragment.changeMenuListener = this
    }

    @SuppressLint("RestrictedApi")
    override fun onBackPressed() {
        if (navMainController!!.backStack.count() == 0) {
            super.onBackPressed()
        } else {
            backPressListener?.onBackPressed()
        }
    }

    override fun onBackPressVisibleChange(isVisible: Boolean, title: String) {
        supportActionBar?.setDisplayHomeAsUpEnabled(isVisible)
        supportActionBar?.title = title
    }

    private fun startVideoProcessingService(outputPath: String, inputPath: String) {
        val intent = Intent(this, VideoProcessingService::class.java).apply {
            putExtra(Constants.OUTPUT_PATH_KEY, outputPath)
            putExtra(Constants.INPUT_PATH_KEY, inputPath)
        }
        startService(intent)
    }

    interface BackPressListener {
        fun onBackPressed()
    }
}