package com.example.elmcompose

import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.example.elmcompose.input.InputMoneyFragment

class MainActivity : FragmentActivity(R.layout.main_activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.container, InputMoneyFragment())
            }
        }
    }
}