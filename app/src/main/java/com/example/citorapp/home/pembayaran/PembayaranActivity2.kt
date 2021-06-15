package com.example.citorapp.home.pembayaran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.citorapp.databinding.ActivityPembayaran2Binding

class PembayaranActivity2 : AppCompatActivity() {

    private lateinit var bindingSecondPayment: ActivityPembayaran2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSecondPayment = ActivityPembayaran2Binding.inflate(layoutInflater)
        setContentView(bindingSecondPayment.root)


        bindingSecondPayment.btnChooseOvo.setOnClickListener(){
            val intent = Intent(this@PembayaranActivity2, PembayaranActivity3::class.java)
            startActivity(intent)
        }

        bindingSecondPayment.btnChooseShopeepay.setOnClickListener(){
            val intent = Intent(this@PembayaranActivity2, PembayaranActivity3::class.java)
            startActivity(intent)
        }
    }
}