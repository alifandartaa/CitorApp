package com.example.citorapp.home.pembayaran

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.citorapp.R
import com.example.citorapp.databinding.ActivityFixPaymentBinding

class FixPaymentActivity : AppCompatActivity() {

    private lateinit var bindingThirdPayment: ActivityFixPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingThirdPayment = ActivityFixPaymentBinding.inflate(layoutInflater)
        setContentView(bindingThirdPayment.root)

        bindingThirdPayment.btnConfirm.setOnClickListener() {
            val view = View.inflate(this@FixPaymentActivity, R.layout.alert_konfirmasi_pembayaran, null)

            val builder = AlertDialog.Builder(this@FixPaymentActivity)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val codePayment = result.data

                bindingThirdPayment.tvCodeValue.text = codePayment.toString()
                bindingThirdPayment.layoutCode.visibility = View.VISIBLE
            }
        }

        bindingThirdPayment.btnMethod.setOnClickListener() {
            startForResult.launch(Intent(this, ChoosePaymentActivity::class.java))
        }
    }
}