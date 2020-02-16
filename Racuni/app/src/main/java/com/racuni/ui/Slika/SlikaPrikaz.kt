package com.racuni.ui.Slika

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.racuni.R
import kotlinx.android.synthetic.main.activity_slika_prikaz.*

class SlikaPrikaz : AppCompatActivity() {

    private lateinit var uri : Uri //Slike


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slika_prikaz)
        //var intent = getIntent()
        //uri = intent.getParcelableExtra("uri")



        gumbPreklici.setOnClickListener {
            finish()
        }
        gumbShrani.setOnClickListener {

        }
    }
}
