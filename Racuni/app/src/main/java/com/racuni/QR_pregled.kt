package com.racuni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_qr_pregled.*
import kotlinx.android.synthetic.main.activity_qrprikaz.*
import kotlinx.android.synthetic.main.activity_qrprikaz.eCena
import kotlinx.android.synthetic.main.activity_qrprikaz.eDatum
import kotlinx.android.synthetic.main.activity_qrprikaz.eIBANPrejemnika
import kotlinx.android.synthetic.main.activity_qrprikaz.eIme
import kotlinx.android.synthetic.main.activity_qrprikaz.eImePrejemnika
import kotlinx.android.synthetic.main.activity_qrprikaz.eKrajPrejemnika
import kotlinx.android.synthetic.main.activity_qrprikaz.eMesto
import kotlinx.android.synthetic.main.activity_qrprikaz.eNamen
import kotlinx.android.synthetic.main.activity_qrprikaz.eNaslov
import kotlinx.android.synthetic.main.activity_qrprikaz.eReferencaPrejemnika
import kotlinx.android.synthetic.main.activity_qrprikaz.eUlicaPrejemnika

class QR_pregled : AppCompatActivity() {

    companion object{
        var ime : String = ""
        var naslov : String = ""
        var mesto : String = ""
        var cena : String = ""
        var namen : String = ""
        var datum : String = ""
        var iBANPrejemnika : String = ""
        var referencaPrejemnika : String = ""
        var imePrejemnika : String = ""
        var ulicaPrejemnika : String = ""
        var krajPrejemnika : String = ""
        var latitude : String = ""
        var longitude : String = ""

/*
        intent.putExtra("latitude", poljeVsehQRDokumentov[position].latitude)
        intent.putExtra("longitude", poljeVsehQRDokumentov[position].longitude)
 */

        /*
                intent.putExtra("Latitude", poljeVsehQRDokumentov[position].Latitude)
        intent.putExtra("Longitude", poljeVsehQRDokumentov[position].Latitude)
        // Toast.makeText(this, lokacija!!.latitude.toString() + "/n" + lokacija!!.longitude.toString(), Toast.LENGTH_LONG).show()
         */
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_pregled)
         ime = intent.getStringExtra("ime")
         naslov = intent.getStringExtra("naslov")
         mesto = intent.getStringExtra("mesto")
         cena = intent.getStringExtra("cena")
         namen = intent.getStringExtra("namen")
         datum = intent.getStringExtra("datum")
         iBANPrejemnika = intent.getStringExtra("iBANPrejemnika")
         referencaPrejemnika = intent.getStringExtra("referencaPrejemnika")
         imePrejemnika = intent.getStringExtra("imePrejemnika")
         ulicaPrejemnika = intent.getStringExtra("ulicaPrejemnika")
         krajPrejemnika = intent.getStringExtra("krajPrejemnika")
         latitude = intent.getStringExtra("latitude")
         longitude = intent.getStringExtra("longitude")


         eIme.setText(ime)
         eNaslov.setText(naslov)
         eMesto.setText(mesto)
         eCena.setText(cena)
         eNamen.setText(namen)
         eDatum.setText(datum)
         eIBANPrejemnika.setText(iBANPrejemnika)
         eReferencaPrejemnika.setText(referencaPrejemnika)
         eImePrejemnika.setText(imePrejemnika)
         eUlicaPrejemnika.setText(ulicaPrejemnika)
         eKrajPrejemnika.setText(krajPrejemnika)

        gumbZapri.setOnClickListener {
            finish()
        }
        gumbLokacija.setOnClickListener {
            if(latitude != "" && longitude != ""){
                var intent = Intent(this, MapaPrikazQR::class.java)
                intent.putExtra("latitude", latitude)
                intent.putExtra("longitude", longitude)
                intent.putExtra("namen", namen)
                //Toast.makeText(this, latitude + "   " + longitude, Toast.LENGTH_LONG).show()
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Lokicije ni bilo mogoče ustrezno pridobiti.", Toast.LENGTH_LONG).show()
            }
        }

    }
}
