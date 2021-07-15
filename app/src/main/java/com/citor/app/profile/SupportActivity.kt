package com.citor.app.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.citor.app.databinding.ActivitySupportBinding

class SupportActivity : AppCompatActivity() {

    private lateinit var supportBinding: ActivitySupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportBinding = ActivitySupportBinding.inflate(layoutInflater)
        setContentView(supportBinding.root)
    }
}