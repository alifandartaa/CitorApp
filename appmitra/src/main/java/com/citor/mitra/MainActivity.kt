package com.citor.mitra

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.citor.mitra.databinding.ActivityMainBinding
import com.citor.mitra.retrofit.AuthService
import com.citor.mitra.retrofit.DataService
import com.citor.mitra.retrofit.RetrofitClient
import com.citor.mitra.retrofit.response.DefaultResponse
import com.citor.mitra.retrofit.response.LoginResponse
import com.citor.mitra.retrofit.response.MitraResponse
import com.citor.mitra.utils.Constants
import com.citor.mitra.utils.MySharedPreferences
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var myPreferences: MySharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        myPreferences = MySharedPreferences(this@MainActivity)

        val idmitra = myPreferences.getValue(Constants.VENDOR_ID).toString()
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
        val ownerName = myPreferences.getValue(Constants.VENDOR_OWNER).toString()
        getStatusBuka(idmitra, tokenAuth)

        mainBinding.tvOwnerName.text = ownerName

        mainBinding.flQueue.setOnClickListener {
            startActivity(Intent(this@MainActivity, ServiceActivity::class.java))
        }

        mainBinding.cvOpenCloseOrder.setOnClickListener {
            val statusBuka = myPreferences.getValue(Constants.VENDOR_STATUS).toString()
            if (statusBuka == "buka") {
                changeBukaTutup(idmitra, "tutup", tokenAuth)
            } else {
                changeBukaTutup(idmitra, "buka", tokenAuth)
            }
        }

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    //stopping the further execution
                    return@OnCompleteListener
                }

                //Getting the token if everything is fine
                val deviceToken = task.result?.token.toString()
                insertToken(idmitra, deviceToken)
            })
        refreshAuthToken(idmitra)
    }

    private fun insertToken(idmitra: String, device_token: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.addToken(idmitra, device_token).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@MainActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }

    private fun refreshAuthToken(idmitra: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.refreshAuthToken(idmitra).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        myPreferences.setValue(Constants.TokenAuth, response.body()!!.tokenAuth)
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toasty.error(this@MainActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }

    private fun changeBukaTutup(idmitra: String, status: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.changeBukaTutup(idmitra, status, "Bearer $tokenAuth").enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        getStatusBuka(idmitra, tokenAuth)
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@MainActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }

    private fun getStatusBuka(idmitra: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getStatusBuka(idmitra, "Bearer $tokenAuth").enqueue(object : Callback<MitraResponse> {
            override fun onResponse(call: Call<MitraResponse>, response: Response<MitraResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        val statusBuka = response.body()!!.data[0].statusBuka
                        myPreferences.setValue(Constants.VENDOR_STATUS, statusBuka)
                        if (statusBuka == "buka") {
                            mainBinding.cvOpenCloseOrder.setCardBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.redClose))
                            mainBinding.tvOpen.text = getString(R.string.tutup_layanan_sekarang)
                            val from = ContextCompat.getColor(this@MainActivity, R.color.md_grey_600)
                            val to = ContextCompat.getColor(this@MainActivity, R.color.primaryColor)
                            bgChangeAnim(from, to)
                            getTotalPesanan(idmitra, tokenAuth)
                        } else {
                            mainBinding.cvOpenCloseOrder.setCardBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.greenOpen))
                            mainBinding.tvOpen.text = getString(R.string.buka_layanan_sekarang)
                            val from = ContextCompat.getColor(this@MainActivity, R.color.primaryColor)
                            val to = ContextCompat.getColor(this@MainActivity, R.color.md_grey_600)
                            bgChangeAnim(from, to)
                            mainBinding.tvInfo.text = getString(R.string.cuci_motor)
                        }
                    }
                }
            }

            private fun bgChangeAnim(from: Int, to: Int) {
                val anim = ValueAnimator()
                anim.setIntValues(from, to)
                anim.setEvaluator(ArgbEvaluator())
                anim.addUpdateListener { valueAnimator -> mainBinding.lnrHello.setBackgroundColor(valueAnimator.animatedValue as Int) }
                anim.duration = 300
                anim.start()
            }

            override fun onFailure(call: Call<MitraResponse>, t: Throwable) {
                Toasty.error(this@MainActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }

    private fun getTotalPesanan(idmitra: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getTotalPesanan(idmitra, "Bearer $tokenAuth").enqueue(object : Callback<MitraResponse> {
            override fun onResponse(call: Call<MitraResponse>, response: Response<MitraResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        val total = response.body()!!.data[0].total
                        val tanggal = response.body()!!.data[0].tanggal
                        mainBinding.tvInfo.text = "Anda Telah Melakukan Pencucian Sebanyak $total Kali Pada Tanggal $tanggal"
                    }
                }
            }

            override fun onFailure(call: Call<MitraResponse>, t: Throwable) {
                Toasty.error(this@MainActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }
}