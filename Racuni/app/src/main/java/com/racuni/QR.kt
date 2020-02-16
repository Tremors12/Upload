package com.racuni


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.integration.android.IntentIntegrator
import com.racuni.ui.Adapterji.AdapterModelQR
import com.racuni.ui.Adapterji.AdapterQR
import com.racuni.ui.razredi.PlaciloQRRazred
import kotlinx.android.synthetic.main.fragment_qr.*

/**
 * A simple [Fragment] subclass.
 */
class QR : Fragment() {
    lateinit var listView : ListView
    var poljeVsehQRDokumentov = mutableListOf<PlaciloQRRazred>()
    var poljeVsehQRDokumentovAdapter = mutableListOf<AdapterModelQR>()
    //View naredimo public
    lateinit var viewFragment : View
    //
    lateinit var besedilo : String


    private var komunikacija : KomunikacijaQR? = null
    interface KomunikacijaQR{
        fun kliciQR()
        fun odpriPrikaz(position : Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewFragment = inflater.inflate(R.layout.fragment_qr, container, false)
        val gumb : FloatingActionButton = viewFragment.findViewById(R.id.gumb)
        gumb.setOnClickListener {
            komunikacija!!.kliciQR()
        }
        listView = viewFragment.findViewById<ListView>(R.id.listView)
        listView.setOnItemClickListener { parent, view, position, id ->

            for(i in 0..poljeVsehQRDokumentov.lastIndex){
                if(position == i){
                    //Log.i("Aplikacija", "Izpis")
                    //Toast.makeText(activity, poljeVsehQRDokumentov[i].toString(), Toast.LENGTH_LONG).show()
                    komunikacija!!.odpriPrikaz(position)
                }
            }
        }


        return viewFragment
    }



    //**********************************************
    //Za komunikacijo
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is KomunikacijaQR){
            komunikacija = context
        }
        else{
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        komunikacija = null
    }
    //************************************************

    /*
    fun sprejetiPodatkiQR(podatki : String){
        //Toast.makeText(activity, podatki, Toast.LENGTH_LONG).show()
        val vrstice = podatki.lines()
        val linije = mutableListOf<String>()
        var besedilo : String = ""
        for(i in vrstice.indices){
            linije.add(vrstice[i].trim())
            besedilo += i.toString() + " = " + linije[i] + "\n"
         }
        //Toast.makeText(activity, linije.toString(), Toast.LENGTH_LONG).show()
        val izpis = viewFragment.findViewById<TextView>(R.id.QRbesedilo)
        izpis.text = besedilo
    }

     */

    fun naloziNovAdapter(poljeVsehQRDokumentovNovo: MutableList<PlaciloQRRazred>){
        //poljeVsehQRDokumentovAdapter = mutableListOf<AdapterModelQR>() //Izbrišemo staro vrednost
        poljeVsehQRDokumentovAdapter.clear()
        poljeVsehQRDokumentov = poljeVsehQRDokumentovNovo
        for(dokument in poljeVsehQRDokumentov){
            poljeVsehQRDokumentovAdapter.add(AdapterModelQR(dokument.ime!!, dokument.naslov!!, dokument.cena!!, dokument.mesto!!))
        }
        listView = viewFragment.findViewById<ListView>(R.id.listView)
        listView.adapter = AdapterQR(context!!, R.layout.vrstica_qr, poljeVsehQRDokumentovAdapter)
        //Toast.makeText(activity, "Izvršeno", Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        val listView = viewFragment.findViewById<ListView>(R.id.listView)
        listView.adapter = AdapterQR(context!!, R.layout.vrstica_qr, poljeVsehQRDokumentovAdapter)
        super.onResume()
    }
}


