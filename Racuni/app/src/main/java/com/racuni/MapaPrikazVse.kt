package com.racuni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaPrikazVse : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapa: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_prikaz_vse)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        var latitude = intent.getStringArrayListExtra("latitude")
        val longitude = intent.getStringArrayListExtra("longitude")
        val namen = intent.getStringArrayListExtra("namen")


        //Toast.makeText(this, latitude.size.toString(), Toast.LENGTH_LONG).show()

        mapa = googleMap
        for(stevec in 0..latitude.lastIndex){
            var mesto = LatLng(latitude[stevec].toDouble(), longitude[stevec].toDouble())
            mapa.addMarker(MarkerOptions().position(mesto).title(namen[stevec]))
            if(stevec == latitude.lastIndex){
                mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(mesto, 19f))
            }
        }




    }
}
