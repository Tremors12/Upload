package com.racuni.ui.razredi

import com.google.firebase.firestore.FirebaseFirestore

class BeriUporabnika(var uporabnik : Uporabnik?) {
    init{
        val firebaseFirestore : FirebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection(uporabnik!!.id + ".podatki")
            .document("uporabnik").get().addOnSuccessListener{dokument ->
                uporabnik = dokument.toObject(Uporabnik::class.java)
            }
    }
}