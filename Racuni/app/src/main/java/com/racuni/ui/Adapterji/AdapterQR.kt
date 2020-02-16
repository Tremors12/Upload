package com.racuni.ui.Adapterji

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.racuni.R
import kotlinx.android.synthetic.main.vrstica_qr.view.*

class AdapterQR(var _context: Context, var resource : Int, var items : List<AdapterModelQR>) : ArrayAdapter<AdapterModelQR>(_context, resource, items){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var layoutInflater : LayoutInflater = LayoutInflater.from(_context)
        val view : View = layoutInflater.inflate(resource, null)

        val Ime : TextView = view.findViewById(R.id.bIme)
        val Naslov : TextView = view.findViewById(R.id.bNaslov)
        val Cena : TextView = view.findViewById(R.id.bCena)
        val Mesto : TextView = view.findViewById(R.id.bMesto)
        var gradniki : AdapterModelQR = items[position]
        Ime.setText(gradniki.ime)
        Naslov.setText(gradniki.naslov)
        Cena.setText(gradniki.cena)
        Mesto.setText(gradniki.mesto)
        return view
    }
}

/*
package com.example.listview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.w3c.dom.Text
import android.widget.Toast.makeText

@Suppress("DEPRECATION")
class Adapter(var _context: Context, var resource : Int, var items : List<Model>)
    : ArrayAdapter<Model>(_context, resource, items){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var layoutInflater : LayoutInflater = LayoutInflater.from(_context)
        val view : View = layoutInflater.inflate(resource, null)

        val slika : ImageView = view.findViewById(R.id.slika)
        val naslov : TextView = view.findViewById(R.id.naslov)
        val opis : TextView = view.findViewById(R.id.opis)
        var gradniki : Model = items[position]
        slika.setImageDrawable(_context.resources.getDrawable(gradniki.slika))
        naslov.text = gradniki.naslov
        opis.text = gradniki.opis

        val gumb : Button = view.findViewById(R.id.gumb)
        gumb.setOnClickListener {
            var i : Int = 10
            Log.i("Adapter", naslov.text.toString())
        }
        return view
    }
}
 */