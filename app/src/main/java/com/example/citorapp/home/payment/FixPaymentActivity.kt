package com.example.citorapp.home.payment

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.citorapp.R
import com.example.citorapp.databinding.ActivityFixPaymentBinding
import com.google.android.material.button.MaterialButton

class FixPaymentActivity : AppCompatActivity() {

    private lateinit var fixPaymentBinding: ActivityFixPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fixPaymentBinding = ActivityFixPaymentBinding.inflate(layoutInflater)
        setContentView(fixPaymentBinding.root)

        fixPaymentBinding.btnBack.setOnClickListener {
            super.onBackPressed()
        }

        fixPaymentBinding.btnConfirmPayment.setOnClickListener() {
            if (fixPaymentBinding.tvCodeValue.text == "0") {
                fixPaymentBinding.btnPaymentMethod.error = "Pilih jenis Pembayaran"
            } else {
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setContentView(R.layout.alert_konfirmasi_pembayaran)
                val btnFinishPaymentDialog = dialog.findViewById<MaterialButton>(R.id.btn_finish_payment_dialog)
                btnFinishPaymentDialog.setOnClickListener {
                    dialog.dismiss()
                    finish()
                }
                dialog.show()
            }
        }

        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val codePayment = result.data

                fixPaymentBinding.tvCodeValue.text = codePayment.toString()
                fixPaymentBinding.layoutCode.visibility = View.VISIBLE
            }
        }

        fixPaymentBinding.btnPaymentMethod.setOnClickListener() {
//            startForResult.launch(Intent(this, ChoosePaymentActivity::class.java))
            customChoosePaymentDialog()
        }
    }

    private fun customChoosePaymentDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.choose_payment_dialog)

        val btnChooseOvo = dialog.findViewById<MaterialButton>(R.id.btn_choose_ovo)
        val btnChooseShopeePay = dialog.findViewById<MaterialButton>(R.id.btn_choose_shopeepay)
        btnChooseOvo.setOnClickListener {
            dialog.dismiss()
            fixPaymentBinding.tvCodeValue.text = "OVO"
            fixPaymentBinding.btnPaymentMethod.text = "OVO"
            fixPaymentBinding.layoutCode.visibility = View.VISIBLE
        }
        btnChooseShopeePay.setOnClickListener {
            dialog.dismiss()
            fixPaymentBinding.tvCodeValue.text = "ShopeePay"
            fixPaymentBinding.btnPaymentMethod.text = "ShopeePay"
            fixPaymentBinding.layoutCode.visibility = View.VISIBLE
        }
        dialog.show()
    }
}