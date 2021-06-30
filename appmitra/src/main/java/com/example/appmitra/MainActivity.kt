package com.example.appmitra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.appmitra.databinding.ActivityMainBinding
import com.example.appmitra.utils.Constants
import com.example.appmitra.utils.MySharedPreferences

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var myPreferences: MySharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        myPreferences = MySharedPreferences(this@MainActivity)

        val ownerName = myPreferences.getValue(Constants.VENDOR_OWNER).toString()
        mainBinding.tvOwnerName.text = ownerName
        Log.e("nama owner", ownerName)

        mainBinding.flQueue.setOnClickListener {
            startActivity(Intent(this@MainActivity, ServiceActivity::class.java))
        }
    }
}