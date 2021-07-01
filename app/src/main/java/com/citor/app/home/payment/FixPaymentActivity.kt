package com.citor.app.home.payment

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.citor.app.R
import com.citor.app.databinding.ActivityFixPaymentBinding
import com.citor.app.utils.Constants
import com.citor.app.utils.MySharedPreferences
import com.google.android.material.button.MaterialButton
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class FixPaymentActivity : AppCompatActivity() {

    private lateinit var fixPaymentBinding: ActivityFixPaymentBinding
    private lateinit var mySharedPreferences: MySharedPreferences

    companion object {
        const val vendorId = "vendor_id"
        const val service = "service"
        const val price = "price"
        const val timeService = "timeService"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        fixPaymentBinding = ActivityFixPaymentBinding.inflate(layoutInflater)
        setContentView(fixPaymentBinding.root)

        mySharedPreferences = MySharedPreferences(this@FixPaymentActivity)

        fixPaymentBinding.btnBack.setOnClickListener {
            super.onBackPressed()
        }

        val vendorId = intent.getStringExtra(vendorId).toString()
        val service = intent.getStringExtra(service).toString()
        val price = intent.getStringExtra(price).toString()
        val timeService = intent.getStringExtra(timeService).toString()

        fixPaymentBinding.tvTypeServiceValue.text = service
        fixPaymentBinding.tvOrderTimeValue.text = timeService
        val numbering = DecimalFormat("#,###")
        fixPaymentBinding.tvPriceValue.text = numbering.format(price.toInt())
        fixPaymentBinding.tvPointValue.text = "10"

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE), 101
            );
        }

        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-5CHnBFylQ2hoYARY")
            .setContext(this)
            .setTransactionFinishedCallback(TransactionFinishedCallback {
                if (it.response != null) {
                    when (it.status) {
                        "success" -> {
                            Log.d("success", "Transaksi ${it.response.transactionId} ${it.response.paymentType} Success")
                        }
                        "pending" -> {
                            Log.d("pending", "Transaksi ${it.response.transactionId} ${it.response.paymentType} Pending")
                        }
                        "invalid" -> {
                            Log.d("invalid", "Transaksi ${it.response.transactionId} ${it.response.paymentType} Invalid")
                        }
                        "failed" -> {
                            Log.d("failed", "Transaksi ${it.response.transactionId} ${it.response.paymentType} Failed")
                        }
                    }
                }
            })
            .setMerchantBaseUrl("https://citor-app.herokuapp.com/index.php/")
            .enableLog(true)
            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .setLanguage("id")
            .buildSDK()

        fixPaymentBinding.btnConfirmPayment.setOnClickListener {
//            val productPrice = fixPaymentBinding.tvPrice.text
            val quantity = 1
            val price = 15000.0
            val totalAmount = quantity * price
            val transactionRequest = TransactionRequest("Citor-APP-" + System.currentTimeMillis().toShort() + "", totalAmount)
            val randomID = UUID.randomUUID().toString()
            val itemDetail = ItemDetails(randomID, price, quantity, "Motor")
            val listItem = ArrayList<ItemDetails>()
            listItem.add(itemDetail)

            uiKitDetail(transactionRequest)
            transactionRequest.itemDetails = listItem

            MidtransSDK.getInstance().transactionRequest = transactionRequest
//            MidtransSDK.getInstance().startPaymentUiFlow(this, PaymentMethod.BANK_TRANSFER_MANDIRI)
            MidtransSDK.getInstance().startPaymentUiFlow(this)
        }
    }

    private fun uiKitDetail(transactionRequest: TransactionRequest) {

//        val customerDetail = CustomerDetails()
//        customerDetail.customerIdentifier = "Alif Andarta"
//        customerDetail.phone = "081217915595"
//        customerDetail.firstName = "Alif"
//        customerDetail.lastName = "Andarta"
//        customerDetail.email = "aliefazuka123@gmail.com"
//        val shippingAddress = ShippingAddress()
//        shippingAddress.address = "Perumdin PTKL F-2 Leces"
//        shippingAddress.city = "Probolinggo"
//        shippingAddress.postalCode = "67273"
//        customerDetail.shippingAddress = shippingAddress
//        val billingAddress = BillingAddress()
//        billingAddress.address = "Perumdin PTKL F-2 Leces"
//        billingAddress.city = "Probolinggo"
//        billingAddress.postalCode = "67273"
//        customerDetail.billingAddress = billingAddress
//
        val customerDetail = CustomerDetails()
        val userName = mySharedPreferences.getValue(Constants.USER_NAMA)
        val userPhone = mySharedPreferences.getValue(Constants.USER_NOHP)
        val userEmail = mySharedPreferences.getValue(Constants.USER_EMAIL)

        customerDetail.customerIdentifier = userName
        customerDetail.phone = userPhone
        customerDetail.email = userEmail
        customerDetail.firstName = userName

        val shippingAddress = ShippingAddress()
        shippingAddress.address = "Malang"
        shippingAddress.city = "Malang"
        shippingAddress.postalCode = "65148"
        customerDetail.shippingAddress = shippingAddress
        val billingAddress = BillingAddress()
        billingAddress.address = "Malang"
        billingAddress.city = "Malang"
        billingAddress.postalCode = "65148"
        customerDetail.billingAddress = billingAddress

//        val shippingAddress = ShippingAddress()
//
//        val billingAddress = BillingAddress()

//        val customerDetail = CustomerDetails()
        transactionRequest.customerDetails = customerDetail
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