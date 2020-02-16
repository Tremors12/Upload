package com.racuni.ui.razredi

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class PosljiUporabnika (val uporabnik : Uporabnik){
    init{
        val firebaseFirestore : FirebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection(uporabnik.id + ".podatki")
            .document("uporabnik").set(uporabnik)
    }
}