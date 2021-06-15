package com.example.citorapp.home.pembayaran

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.citorapp.R
import com.example.citorapp.databinding.ActivityPembayaran1Binding
import com.example.citorapp.databinding.ActivityPembayaran3Binding

class PembayaranActivity3 : AppCompatActivity() {

    private lateinit var bindingThirdPayment: ActivityPembayaran3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingThirdPayment = ActivityPembayaran3Binding.inflate(layoutInflater)
        setContentView(bindingThirdPayment.root)

        bindingThirdPayment.btnConfirm.setOnClickListener(){
            val view = View.inflate(this@PembayaranActivity3, R.layout.alert_konfirmasi_pembayaran, null)

            val builder = AlertDialog.Builder(this@PembayaranActivity3)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }


    }
}