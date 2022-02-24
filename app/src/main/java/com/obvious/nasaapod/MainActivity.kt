package com.obvious.nasaapod

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.obvious.nasaapod.home.view.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, HomeFragment(), HomeFragment::class.java.name)
            .commit()
    }
}