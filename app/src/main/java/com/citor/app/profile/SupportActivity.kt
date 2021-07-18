package com.citor.app.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.citor.app.MainActivity
import com.citor.app.R
import com.citor.app.databinding.ActivitySupportBinding
import java.net.URLEncoder


class SupportActivity : AppCompatActivity() {

    private lateinit var supportBinding: ActivitySupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportBinding = ActivitySupportBinding.inflate(layoutInflater)
        setContentView(supportBinding.root)

        supportBinding.cvMail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("citorapps@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "")
            intent.putExtra(Intent.EXTRA_TEXT, "")
            startActivity(Intent.createChooser(intent, "Pilih Email"))
        }

        supportBinding.cvWhatssapp.setOnClickListener {
            val number = getString(R.string.support_number)
            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://api.whatsapp.com/send?phone=$number" + "&text=" + URLEncoder.encode("", "UTF-8"))
                )
            startActivity(intent)
        }

        supportBinding.cvInstagram.setOnClickListener {
            val uri = Uri.parse("https://www.instagram.com/citor.official/")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)
            startActivity(likeIng)
        }

        supportBinding.btnNext.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

}