package com.racuni

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_qrprikaz.*

class QRPrikaz : AppCompatActivity() {

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
    }


    lateinit var podatki : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrprikaz)
        podatki = intent.getStringExtra("podatki")


        val vrstice = podatki.lines()
        val linije = mutableListOf<String>()
        //var besedilo : String = ""
        for(vrstica in vrstice.indices){
            when(vrstica){
                5 -> ime  = vrstice[vrstica].trim()
                6 -> naslov = vrstice[vrstica].trim()
                7 -> mesto = vrstice[vrstica].trim()
                8 -> cena = (vrstice[vrstica].trim().toDouble() / 100).toString() + " euro"
                12 -> namen = vrstice[vrstica].trim()
                13 -> datum = vrstice[vrstica].trim()
                14 -> iBANPrejemnika = vrstice[vrstica].trim()
                15 -> referencaPrejemnika = vrstice[vrstica].trim()
                16 -> imePrejemnika = vrstice[vrstica].trim()
                17 -> ulicaPrejemnika  = vrstice[vrstica].trim()
                18 -> krajPrejemnika = vrstice[vrstica].trim()
            }
            //besedilo += vrstica.toString() + ".=" +  vrstice[vrstica] + "\n"
        }
        //Toast.makeText(this, besedilo, Toast.LENGTH_LONG).show()
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

        gumbShraniQR.setOnClickListener {
            var intent = Intent()
            intent.putExtra("ime", ime)
            intent.putExtra("naslov", naslov)
            intent.putExtra("mesto", mesto)
            intent.putExtra("cena", cena)
            intent.putExtra("namen", namen)
            intent.putExtra("datum", datum)
            intent.putExtra("iBANPrejemnika", iBANPrejemnika)
            intent.putExtra("referencaPrejemnika", referencaPrejemnika)
            intent.putExtra("imePrejemnika", imePrejemnika)
            intent.putExtra("ulicaPrejemnika", ulicaPrejemnika)
            intent.putExtra("krajPrejemnika", krajPrejemnika)

            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        gumbPrekliciQR.setOnClickListener {
            finish()
        }

    }


}

