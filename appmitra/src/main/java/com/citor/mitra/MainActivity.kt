package com.citor.mitra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.citor.mitra.utils.Constants
import com.citor.mitra.utils.MySharedPreferences
import com.citor.mitra.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var myPreferences: MySharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        myPreferences = MySharedPreferences(this@MainActivity)

        val ownerName = myPreferences.getValue(Constants.VENDOR_OWNER).toString()
        mainBinding.tvOwnerName.text = ownerName
        Log.e("nama owner", ownerName)

        mainBinding.flQueue.setOnClickListener {
            startActivity(Intent(this@MainActivity, ServiceActivity::class.java))
        }
    }
}