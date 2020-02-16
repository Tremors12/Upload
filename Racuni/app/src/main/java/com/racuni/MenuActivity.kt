package com.racuni

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import com.racuni.ui.Slika.SlikaPrikaz
import com.racuni.ui.razredi.NaloziQRPodatke
import com.racuni.ui.razredi.PlaciloQRRazred
import com.racuni.ui.razredi.PosljiUporabnika
import com.racuni.ui.razredi.Uporabnik
import kotlinx.android.synthetic.main.activity_qrprikaz.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

const val KODA_QR_INTENT : Int = 0
//const val ID_SLIKA_GALERIJA : Int = 1 //Za actvivity, ki sprejema slike
//const val KODA_SLIKA_INTENT : Int = 2


class MenuActivity : AppCompatActivity(), QR.KomunikacijaQR {

    //Lokacija
    var lokacija: Location? = null
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    //Javne konstante
    lateinit var fragmentQR : QR
    lateinit var fragmentRacun : Racun
    lateinit var fragmentOAplikaciji: FragmentOAplikaciji
    //Uporabnik
    lateinit var uporabnik : FirebaseUser
    lateinit var uporabnikRazred : Uporabnik
    //
    lateinit var firestore: FirebaseFirestore
    //************************************
    var poljeVsehQRDokumentov = mutableListOf<PlaciloQRRazred>()
    //
    lateinit var celotnoBesediloQR : String
    //Slika
    //private lateinit var uri : Uri //Slike


    override fun kliciQR() {
        var QRintegrator : IntentIntegrator = IntentIntegrator(this)
        QRintegrator.setPrompt("Skenirajte QR kodo")
        QRintegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        QRintegrator.initiateScan()
    }

    lateinit var draw : DrawerLayout //Za menu


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//Dodado da preprečimo obračanje

        firestore = FirebaseFirestore.getInstance()

        //****MENU
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        draw = findViewById(R.id.drawer_layout)
        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        var toggle = ActionBarDrawerToggle(this, draw, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        draw.addDrawerListener(toggle)
        toggle.syncState()
        //************************
        //Naredimo instance od fragmentov
        fragmentQR = QR()
        fragmentRacun = Racun()
        fragmentOAplikaciji = FragmentOAplikaciji()
        //Na začetno stran prikažemo prvi fragment
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragmentQR).commit()
        //Preklaplanje med fragmenti
        navigationView.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.nav_QR ->
                {
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragmentQR).commit()
                }
                R.id.nav_racun -> {
                    var intent = Intent(this, MapaPrikazVse::class.java)
                    var listNamen = ArrayList<String>()
                    var listLatitude =  ArrayList<String>()
                    var listLongitude =  ArrayList<String>()
                    for(stevec in poljeVsehQRDokumentov.indices){
                        listNamen.add(poljeVsehQRDokumentov[stevec].namen!!)
                        listLatitude.add(poljeVsehQRDokumentov[stevec].latitude!!)
                        listLongitude.add(poljeVsehQRDokumentov[stevec].longitude!!)
                    }
                    intent.putExtra("namen", listNamen)
                    intent.putExtra("latitude", listLatitude)
                    intent.putExtra("longitude", listLongitude)
                    startActivity(intent)
                }
                R.id.nav_OAplikaciji ->{supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragmentOAplikaciji).commit()}
                R.id.nav_odjava ->{
                    AuthUI.getInstance().signOut(this)
                    var intent : Intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
            }
            true
        }


        //MOJA KODA*****************************************************************
        //Dobimo vse začetne QR dokumente
        //*******************************
        var intent = getIntent()
        uporabnik = intent.getParcelableExtra("uporabnik") //Pridobimo razred o uporabniku
        uporabnikRazred = Uporabnik(uporabnik.displayName, uporabnik.uid, uporabnik.email, uporabnik.phoneNumber)
        PosljiUporabnika(uporabnikRazred)//Razred o uporabniku vpišemo v podatkovno bazo
        //Dodamo QR dokumente
        firestore.collection(uporabnikRazred.id.toString() + "." + uporabnikRazred.ime + ".QR" ).get()
        //dobiZacetneQRDokumente()


        //Naredimo listener za dogoke v podatkovni bazi
        firestore.collection(uporabnikRazred.id.toString() + "." + uporabnikRazred.ime + ".QR").addSnapshotListener {

                querySnapshot, firebaseFirestoreException ->
            if(querySnapshot != null) {
                poljeVsehQRDokumentov.clear()
                for (dokument in querySnapshot) {
                    poljeVsehQRDokumentov.add(dokument.toObject(PlaciloQRRazred::class.java))
                    Log.i("Aplikacija", dokument.toObject(PlaciloQRRazred::class.java).toString())
                }
                //Toast.makeText(this, querySnapshot.size().toString(), Toast.LENGTH_LONG).show()
                fragmentQR.naloziNovAdapter(poljeVsehQRDokumentov)
            }
        }
        //narediSliko()

        //Lokacija
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
    }


    //Za branje QR kode
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            if(data != null){
                when(requestCode){
                    KODA_QR_INTENT -> {
                        val ime = data.getStringExtra("ime")
                        val naslov = data.getStringExtra("naslov")
                        val mesto = data.getStringExtra("mesto")
                        val cena = data.getStringExtra("cena")
                        val namen = data.getStringExtra("namen")
                        val datum = data.getStringExtra("datum")
                        val iBANPrejemnika = data.getStringExtra("iBANPrejemnika")
                        val referencaPrejemnika = data.getStringExtra("referencaPrejemnika")
                        val imePrejemnika = data.getStringExtra("imePrejemnika")
                        val ulicaPrejemnika = data.getStringExtra("ulicaPrejemnika")
                        val krajPrejemnika = data.getStringExtra("krajPrejemnika")

                        val placiloQRRazred : PlaciloQRRazred = PlaciloQRRazred(ime = ime, naslov = naslov,mesto = mesto, cena = cena,
                            namen = namen, datum = datum, ibanprejemnika = iBANPrejemnika, referencaPrejemnika = referencaPrejemnika,
                            imePrejemnika = imePrejemnika, ulicaPrejemnika = ulicaPrejemnika, krajPrejemnika = krajPrejemnika, latitude = lokacija!!.latitude.toString(), longitude = lokacija!!.longitude.toString())

                        //Toast.makeText(this, placiloQRRazred.toString(), Toast.LENGTH_LONG).show()
                        //NaloziQRPodatke().nalozi(placiloQRRazred, uporabnikRazred)
                        //Toast.makeText(this, NaloziQRPodatke().dobiSteviloQRpodatkov(uporabnikRazred), Toast.LENGTH_LONG).show()
                        GlobalScope.launch{//Naložimo podatke v podatkovno bazo
                            //firestore = FirebaseFirestore.getInstance()
                            firestore.collection(uporabnikRazred.id.toString() + "." + uporabnikRazred.ime + ".QR" ).get().addOnSuccessListener {
                                val steviloElementov = it.count()
                                NaloziQRPodatke().nalozi(placiloQRRazred, uporabnikRazred, steviloElementov)
                                poljeVsehQRDokumentov.add(placiloQRRazred)
                                fragmentQR.naloziNovAdapter(poljeVsehQRDokumentov)
                            }
                        }
                        //Sedaj je čas za posodobitev list viewa
                        //poljeVsehQRDokumentov.add(placiloQRRazred)
                        //fragmentQR.naloziNovAdapter(poljeVsehQRDokumentov)
                    }
                    /*
                    ID_SLIKA_GALERIJA->{//Sliko je uspešno uspelo naložiti
                        if(data?.data != null && data != null){
                            try {
                                uri = data!!.data!!
                                Toast.makeText(this, "Uspelo", Toast.LENGTH_LONG).show()

                            }catch (e : Exception){
                                Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                     */

                    else -> {
                        val result  = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                        if(result != null) {
                            if(result.getContents() == null) {
                                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                            } else {
                                //TO POMENI DA SMO SPREJELI PODATKE IZ QR USPEŠNO KLIČEMO NOV INTENT
                                //fragmentQR.sprejetiPodatkiQR(result.contents)
                                //celotnoBesediloQR = result.contents
                                getLastLocation()
                                var intent = Intent(this, QRPrikaz::class.java)
                                intent.putExtra("podatki", result.contents)
                                startActivityForResult(intent, KODA_QR_INTENT)

                            }
                        }
                        else {
                            super.onActivityResult(requestCode, resultCode, data);
                        }
                    }
                }
            }
        }

    }


    override fun odpriPrikaz(position: Int) {
        //Toast.makeText(this, poljeVsehQRDokumentov[position].toString(), Toast.LENGTH_LONG).show()
        //Toast.makeText(this, "Tukaj", Toast.LENGTH_LONG).show()
        var intent = Intent(this, QR_pregled::class.java)
        intent.putExtra("ime", poljeVsehQRDokumentov[position].ime)
        intent.putExtra("naslov", poljeVsehQRDokumentov[position].naslov)
        intent.putExtra("mesto", poljeVsehQRDokumentov[position].mesto)
        intent.putExtra("cena", poljeVsehQRDokumentov[position].cena)
        intent.putExtra("namen", poljeVsehQRDokumentov[position].namen)
        intent.putExtra("datum", poljeVsehQRDokumentov[position].datum)
        intent.putExtra("iBANPrejemnika", poljeVsehQRDokumentov[position].ibanprejemnika)
        intent.putExtra("referencaPrejemnika", poljeVsehQRDokumentov[position].referencaPrejemnika)
        intent.putExtra("imePrejemnika", poljeVsehQRDokumentov[position].imePrejemnika)
        intent.putExtra("ulicaPrejemnika", poljeVsehQRDokumentov[position].ulicaPrejemnika)
        intent.putExtra("krajPrejemnika", poljeVsehQRDokumentov[position].krajPrejemnika)
        intent.putExtra("latitude", poljeVsehQRDokumentov[position].latitude)
        intent.putExtra("longitude", poljeVsehQRDokumentov[position].longitude)

        //Toast.makeText(this@MenuActivity, poljeVsehQRDokumentov[position].latitude.toString(), Toast.LENGTH_LONG).show()

        startActivity(intent)
        //Log.i("Aplikacija", "Zaganjam activity")

        /*
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
         */
    }


/*
    //Dela slike
    private fun narediSliko() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, ID_SLIKA_GALERIJA)
            }
        }
    }
    //Slike

 */

    //firestore.collection(uporabnikRazred.id.toString() + "." + uporabnikRazred.ime + ".QR")


    /*
    fun dobiZacetneQRDokumente(){
        Toast.makeText(this, "Izvršila se je funkcija!!", Toast.LENGTH_LONG).show()
        GlobalScope.launch {
           // firestore = FirebaseFirestore.getInstance()
            firestore.collection(uporabnikRazred.id.toString() + "." + uporabnikRazred.ime + ".QR")
                    .get().addOnSuccessListener {dokumenti->
                        for(dokument in dokumenti){
                           poljeVsehQRDokumentov.add(dokument.toObject(PlaciloQRRazred::class.java))
                           Log.i("Aplikacija", dokument.toObject(PlaciloQRRazred::class.java).toString())
                        }
                    }
            if(poljeVsehQRDokumentov.isNotEmpty()){
                fragmentQR.naloziNovAdapter(poljeVsehQRDokumentov)
            }
        }
    }
     */


        @SuppressLint("MissingPermission")
        private fun getLastLocation() {
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                        lokacija = task.result
                        if (lokacija == null) {
                            requestNewLocationData()
                        } else {
                            //findViewById<TextView>(R.id.latTextView).text = location.latitude.toString()
                            //findViewById<TextView>(R.id.lonTextView).text = location.longitude.toString()

                            //Toast.makeText(this, lokacija!!.latitude.toString() + "/n" + lokacija!!.longitude.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    //Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            } else {
                requestPermissions()
            }
        }

        @SuppressLint("MissingPermission")
        private fun requestNewLocationData() {
            var mLocationRequest = LocationRequest()
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            mLocationRequest.interval = 0
            mLocationRequest.fastestInterval = 0
            mLocationRequest.numUpdates = 1

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            mFusedLocationClient!!.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
            )
        }

        private val mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                var mLastLocation: Location = locationResult.lastLocation
                //findViewById<TextView>(R.id.latTextView).text = mLastLocation.latitude.toString()
                //findViewById<TextView>(R.id.lonTextView).text = mLastLocation.longitude.toString()
            }
        }

        private fun isLocationEnabled(): Boolean {
            var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }

        private fun checkPermissions(): Boolean {
            if (ActivityCompat.checkSelfPermission(
                    this@MenuActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this@MenuActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
            return false
        }

        private fun requestPermissions() {
            ActivityCompat.requestPermissions(
                this@MenuActivity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ID
            )
        }


        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
            if (requestCode == PERMISSION_ID) {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLastLocation()
                }
            }
        }
}
