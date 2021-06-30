package com.citor.app.auth

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.citor.app.R
import com.citor.app.databinding.ActivityAuthBinding
import com.google.android.material.tabs.TabLayoutMediator

class AuthActivity : AppCompatActivity() {

    private lateinit var authBinding: ActivityAuthBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authBinding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(authBinding.root)

        val sectionsPagerAdapter = AuthPagerAdapter(this)
        authBinding.vpAuth.adapter = sectionsPagerAdapter
        TabLayoutMediator(authBinding.tabsAuth, authBinding.vpAuth) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }
}