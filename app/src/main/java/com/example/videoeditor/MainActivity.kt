package com.example.videoeditor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.videoeditor.ui.BaseFragment

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
}