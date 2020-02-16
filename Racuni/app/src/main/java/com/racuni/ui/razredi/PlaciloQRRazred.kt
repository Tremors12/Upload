package com.racuni.ui.razredi

data class PlaciloQRRazred(val ime : String? = null, val naslov : String? = null, val mesto : String? = null, val cena : String? = null, val namen : String? = null, val datum : String? = null,
                           val ibanprejemnika : String? = "", val referencaPrejemnika : String? = "", val imePrejemnika : String? = "",
                           val ulicaPrejemnika : String? = "", val krajPrejemnika : String? = "",
                           val latitude : String? = "", val longitude : String? = "")
//                           Toast.makeText(this, location!!.latitude.toString() + "/n" + location!!.longitude.toString(), Toast.LENGTH_LONG).show()
