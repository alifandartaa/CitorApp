package com.citor.app.home.payment

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.citor.app.R
import com.citor.app.databinding.ActivityFixPaymentBinding
import com.citor.app.retrofit.DataService
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.DefaultResponse
import com.citor.app.utils.Constants
import com.citor.app.utils.MySharedPreferences
import com.citor.app.utils.NotificationHelper
import com.google.android.material.button.MaterialButton
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        val tokenAuth = mySharedPreferences.getValue(Constants.TokenAuth).toString()
        val idUser = mySharedPreferences.getValue(Constants.USER_ID).toString()

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

        fixPaymentBinding.btnPaymentMethod.setOnClickListener() {
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
            .setTransactionFinishedCallback { result ->
                if (result.response != null) {
                    val transactionId = result.response.transactionId
                    val paymentType = result.response.paymentType
                    val transactionStatus = result.status
                    when (result.status) {
                        "success" -> {
//                            Log.d("success", "Transaksi ${result.response.transactionId} ${result.response.paymentType} Success")
                            updateStatusTransaction(tokenAuth, vendorId, idUser, transactionId, paymentType, transactionStatus)

                        }
                        "pending" -> {
                            //aksi 1. membuat button jam cuci mitra yang dipesan menjadi abu abu
                            disableButtonWashHour(tokenAuth)

                            //aksi 2. update status pencucian di database menjadi pending
                            updateStatusTransaction(tokenAuth, vendorId, idUser, transactionId, paymentType, transactionStatus)
//                            Log.d("pending", "Transaksi ${result.response.transactionId} ${result.response.paymentType} Pending")
                        }
                        "failed" -> {
                            disableButtonWashHour(tokenAuth)

                            //aksi 1. membuat button jam cuci mitra yang dipesan menjadi hijau lagi
                            updateStatusTransaction(tokenAuth, vendorId, idUser, transactionId, paymentType, transactionStatus)
//                            Log.d("failed", "Transaksi ${result.response.transactionId} ${result.response.paymentType} Failed")
                        }
                    }
                }
            }
            .setMerchantBaseUrl("https://citor-app.herokuapp.com/index.php/")
            .enableLog(true)
            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .setLanguage("id")
            .buildSDK()

        fixPaymentBinding.btnConfirmPayment.setOnClickListener {
            val quantity = 1
            val totalAmount = quantity * price.toDouble()
            val transactionRequest = TransactionRequest("Citor-APP-" + System.currentTimeMillis().toShort() + "", totalAmount)
            val randomID = UUID.randomUUID().toString()
            val itemDetail = ItemDetails(randomID, price.toDouble(), quantity, "Motor")
            val listItem = ArrayList<ItemDetails>()
            listItem.add(itemDetail)

            uiKitDetail(transactionRequest)
            transactionRequest.itemDetails = listItem

            MidtransSDK.getInstance().transactionRequest = transactionRequest
            MidtransSDK.getInstance().startPaymentUiFlow(this)
        }
    }

    private fun disableButtonWashHour(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.changeStatus(timeService, "tutup", "Bearer $tokenAuth").enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@FixPaymentActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }

    private fun updateStatusTransactionToPending() {

    }

    private fun updateStatusTransaction(
        tokenAuth: String,
        vendorId: String,
        idUser: String,
        transactionId: String?,
        paymentType: String?,
        transactionStatus: String?
    ) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.insertPemesanan(vendorId, idUser, transactionId!!, paymentType!!, transactionStatus!!, "Bearer $tokenAuth").enqueue(object :
            Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        val title = "Pembayaran Selesai!"
                        val body = "Selamat! Kamu Mendapatkan 10 Poin, Silahkan Cek Jumlah Poin Pada Beranda Aplikasi"
                        NotificationHelper(this@FixPaymentActivity).displayNotification(title, body)
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@FixPaymentActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }

    private fun uiKitDetail(transactionRequest: TransactionRequest) {

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