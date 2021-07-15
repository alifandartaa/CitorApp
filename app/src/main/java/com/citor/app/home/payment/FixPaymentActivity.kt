package com.citor.app.home.payment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.citor.app.MainActivity
import com.citor.app.R
import com.citor.app.databinding.ActivityFixPaymentBinding
import com.citor.app.retrofit.DataService
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.DefaultResponse
import com.citor.app.utils.Constants
import com.citor.app.utils.MySharedPreferences
import com.citor.app.utils.NotificationHelper
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
        const val vendorName = "nama_mitra"
        const val vendorId = "vendor_id"
        const val service = "service"
        const val price = "price"
        const val timeService = "timeService"
        const val idJamBuka = "idJamBuka"
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

        val vendorNameOrdered = intent.getStringExtra(vendorName).toString()
        val vendorId = intent.getStringExtra(vendorId).toString()
        val service = intent.getStringExtra(service).toString()
        val price = intent.getStringExtra(price).toString()
        val timeService = intent.getStringExtra(timeService).toString()
        val idJamBuka = intent.getStringExtra(idJamBuka).toString()

        fixPaymentBinding.tvVendorName.text = vendorNameOrdered
        fixPaymentBinding.tvTypeServiceValue.text = service
        fixPaymentBinding.tvOrderTimeValue.text = timeService
        val numbering = DecimalFormat("#,###")
        fixPaymentBinding.tvPriceValue.text = numbering.format(price.toInt())
        fixPaymentBinding.tvPointValue.text = "5"

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
                    when (result.status) {
                        "success" -> {
//                            Log.d("success", "Transaksi ${result.response.transactionId} ${result.response.paymentType} Success")
                            val poin = mySharedPreferences.getValue(Constants.USER_POIN).toString()
                            val poinInt = poin.toInt() + 5
                            mySharedPreferences.setValue(Constants.USER_POIN, poinInt.toString())
                            changeStatus(idJamBuka, "penuh", tokenAuth)

                            updateStatusTransaction(tokenAuth, vendorId, idUser, idJamBuka, service,transactionId, paymentType, price,"berjalan")
                            startActivity(Intent(this@FixPaymentActivity, MainActivity::class.java))
                            finish()
                        }
                        "pending" -> {
                            //aksi 1. membuat button jam cuci mitra yang dipesan menjadi abu abu
                            changeStatus(idJamBuka, "kunci", tokenAuth)

                            //aksi 2. update status pencucian di database menjadi pending
//                            updateStatusTransaction(tokenAuth, vendorId, idUser, transactionId, paymentType, transactionStatus)
//                            Log.d("pending", "Transaksi ${result.response.transactionId} ${result.response.paymentType} Pending")
                        }
                        "failed" -> {
                            changeStatus(idJamBuka, "tersedia", tokenAuth)

                            //aksi 1. membuat button jam cuci mitra yang dipesan menjadi hijau lagi
//                            updateStatusTransaction(tokenAuth, vendorId, idUser, transactionId, paymentType, transactionStatus)
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
            val itemDetail = ItemDetails(randomID, price.toDouble(), quantity, service)
            val listItem = ArrayList<ItemDetails>()
            listItem.add(itemDetail)

            uiKitDetail(transactionRequest)
            transactionRequest.itemDetails = listItem

            MidtransSDK.getInstance().transactionRequest = transactionRequest
            MidtransSDK.getInstance().startPaymentUiFlow(this)
        }
    }

    private fun changeStatus(idJamBuka: String, status: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.changeStatus(idJamBuka, status, "Bearer $tokenAuth").enqueue(object : Callback<DefaultResponse> {
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

    private fun updateStatusTransaction(
        tokenAuth: String,
        vendorId: String,
        idUser: String,
        idJamBuka: String,
        layanan: String,
        transactionId: String?,
        paymentType: String?,
        price: String,
        transactionStatus: String?
    ) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.insertPemesanan(vendorId, idUser, idJamBuka, layanan, transactionId!!, paymentType!!, price, transactionStatus!!, "Bearer $tokenAuth")
            .enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status == "success") {
                            val title = "Pembayaran Selesai!"
                            val body = "Selamat! Kamu Mendapatkan 5 Poin, Silahkan Cek Jumlah Poin Pada Beranda Aplikasi"
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
}