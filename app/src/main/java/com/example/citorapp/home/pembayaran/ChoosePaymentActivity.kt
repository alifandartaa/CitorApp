package com.example.citorapp.home.pembayaran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.citorapp.databinding.ActivityChoosePaymentBinding


class ChoosePaymentActivity : AppCompatActivity() {

    private lateinit var bindingSecondPayment: ActivityChoosePaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSecondPayment = ActivityChoosePaymentBinding.inflate(layoutInflater)
        setContentView(bindingSecondPayment.root)

        bindingSecondPayment.btnChooseOvo.setOnClickListener(){
            val intent = Intent(this@ChoosePaymentActivity, FixPaymentActivity::class.java)
            intent.putExtra("EXTRA_OVO", "OVO")
            setResult(RESULT_OK, intent)
            startActivity(intent)
        }

        bindingSecondPayment.btnChooseShopeepay.setOnClickListener(){
            val intent = Intent(this@ChoosePaymentActivity, FixPaymentActivity::class.java)
            intent.putExtra("EXTRA_SHOPEE", "ShopeePay")
            setResult(RESULT_OK, intent)
            startActivity(intent)
        }
    }
}