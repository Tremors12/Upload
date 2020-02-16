package com.racuni.ui.razredi

import com.google.firebase.firestore.FirebaseFirestore

class NaloziQRPodatke {
    fun nalozi(placiloQRRazred: PlaciloQRRazred, uporabnik: Uporabnik, steviloObstojecihElementov : Int){
        val firebaseFirestore : FirebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection(uporabnik.id.toString() + "." + uporabnik.ime + ".QR")
            .document((steviloObstojecihElementov + 1).toString()).set(placiloQRRazred)

    }
}