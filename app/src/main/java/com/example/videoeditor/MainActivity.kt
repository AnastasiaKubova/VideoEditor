package com.example.videoeditor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.videoeditor.service.VideoProcessingService
import com.example.videoeditor.ui.BaseFragment
import com.example.videoeditor.util.Constants

class MainActivity : AppCompatActivity(), BaseFragment.ChangeMenuListener {

    private var navHostMainFragment: NavHostFragment? = null
    private var navMainController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Init fragments. */
        navHostMainFragment = supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment
        navMainController = navHostMainFragment!!.navController
    }

    override fun onResume() {
        super.onResume()
        BaseFragment.changeMenuListener = this
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
}