package com.example.citorapp.home.pembayaran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.citorapp.R
import com.example.citorapp.databinding.ActivityPembayaran1Binding

class PembayaranActivity1 : AppCompatActivity() {

    private lateinit var bindingFirstPayment: ActivityPembayaran1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingFirstPayment = ActivityPembayaran1Binding.inflate(layoutInflater)
        setContentView(bindingFirstPayment.root)

        bindingFirstPayment.btnConfirm.setOnClickListener(){
            val view = View.inflate(this@PembayaranActivity1, R.layout.alert_konfirmasi_pembayaran, null)

            val builder = AlertDialog.Builder(this@PembayaranActivity1)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        }

        bindingFirstPayment.btnMethod.setOnClickListener(){
            val intent = Intent(this@PembayaranActivity1, PembayaranActivity2::class.java)
            startActivity(intent)
        }

    }
}