package com.racuni

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

private const val KODA_PRIJAVA = 0 //Koda za prijavo

class MainActivity : AppCompatActivity() {

    lateinit var providers : List<AuthUI.IdpConfig> //Potrebujemo za delo z loginom
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gumb_prijava.setOnClickListener {
            showSingInOptions()
        }


        providers = Arrays.asList<AuthUI.IdpConfig>(        //Delo z loginom
            AuthUI.IdpConfig.EmailBuilder().build(),        //Email
            AuthUI.IdpConfig.GoogleBuilder().build(),     //Google
            AuthUI.IdpConfig.PhoneBuilder().build()     //Telefon
        )

    }

    private fun showSingInOptions() {   //Funkcija, ki odpre prijavljanje
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.OzadjePrijava)
            .build(), KODA_PRIJAVA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == KODA_PRIJAVA){//PRIJAVA USPEŠNA ODPRE NAJ SE NOVI ACTIVITY IN ZAPRE STARI
            val uporabnik = FirebaseAuth.getInstance().currentUser
            var intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("uporabnik", uporabnik)
            startActivity(intent)
            finish() //Ko se odpre nov ACTIVITY končamo starega
        }
    }
}


/*
package com.example.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val MY_REQUEST_CODE = 7117 //Poljubna vrednost
    lateinit var providers : List<AuthUI.IdpConfig>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),        //Email
        //    AuthUI.IdpConfig.FacebookBuilder().build(),     //Facebook
            AuthUI.IdpConfig.GoogleBuilder().build(),     //Google
            AuthUI.IdpConfig.PhoneBuilder().build()     //Telefon
        )
        showSingInOptions()


        button_singOut.setOnClickListener{
                       AuthUI.getInstance().signOut(this@MainActivity)
                .addOnSuccessListener {
                    button_singOut.isEnabled = false
                    showSingInOptions()
                }
                .addOnFailureListener {
                        e -> Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
                }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = IdpResponse.fromResultIntent(data)
        if(resultCode == Activity.RESULT_OK){
            val user = FirebaseAuth.getInstance().currentUser  //Uporabnik
            //Toast.makeText(this, "" + user!!.email, Toast.LENGTH_LONG).show()
            //button_singOut.isEnabled = true
            var intent = Intent(this, Main2Activity::class.java)
            startActivity(intent)
        }
        else{
            Toast.makeText(this, "" + response!!.error!!.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun showSingInOptions() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
          .setAvailableProviders(providers)
            .setTheme(R.style.AppTheme)
            .build(), MY_REQUEST_CODE)
    }



}

 */