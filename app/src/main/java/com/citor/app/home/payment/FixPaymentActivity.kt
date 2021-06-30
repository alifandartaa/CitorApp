package com.citor.app.home.payment

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
import com.citor.app.R
import com.citor.app.databinding.ActivityFixPaymentBinding
import com.google.android.material.button.MaterialButton
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import java.text.DecimalFormat


class FixPaymentActivity : AppCompatActivity() {

    private lateinit var fixPaymentBinding: ActivityFixPaymentBinding

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
        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-5CHnBFylQ2hoYARY") //
            .setContext(applicationContext) // context is mandatory
            .setTransactionFinishedCallback {
                result->
            } // set transaction finish callback (sdk callback)
            .setMerchantBaseUrl("localhost/midtrans-citor/") //set merchant url (required)
            .enableLog(true) // enable sdk log (optional)
            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // set  theme. it will replace theme on snap theme on MAP ( optional)
            .setLanguage("id") //`en` for English and `id` for Bahasa
            .buildSDK()

        fixPaymentBinding.btnConfirmPayment.setOnClickListener {
//            val productPrice = fixPaymentBinding.tvPrice.text
            val productPrice = 15000.0
            val transactionRequest = TransactionRequest("Citor App +"+System.currentTimeMillis().toShort()+"", productPrice)
            val detailsItem = ItemDetails("Nama Item Id",productPrice,1,"Layanan cuci motor")
            val itemDetails = ArrayList<ItemDetails>()
            itemDetails.add(detailsItem)

            uiKitdetails(transactionRequest,"Citor App")

            MidtransSDK.getInstance().transactionRequest = transactionRequest
            MidtransSDK.getInstance().startPaymentUiFlow(this)
        }
    }

    private fun uiKitdetails(transactionRequest: TransactionRequest, name: String){
        //get nama customer
        val customerDetails = CustomerDetails()
        customerDetails.customerIdentifier = "Coba nama"
        customerDetails.phone = "08123123123"
        customerDetails.firstName = "Coba"
        customerDetails.lastName = "nama"
        customerDetails.email = "coba@gmail.com"

        val shippingAddress = ShippingAddress()
        shippingAddress.address = "Nahelop"
        shippingAddress.city ="Malang"
        shippingAddress.postalCode = "656121"
        customerDetails.shippingAddress = shippingAddress

        val billingAddress = BillingAddress()
        billingAddress.address = "Nahelop"
        billingAddress.city ="Malang"
        billingAddress.postalCode = "656121"
        customerDetails.billingAddress = billingAddress

        transactionRequest.customerDetails = customerDetails
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