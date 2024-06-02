package com.miguel.jobharbor2.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Usuario(
    var ID: Int = 0,
    var nombre: String = "",
    var ape: String = "",
    var contra: String = "",
    var email: String = "",
) : Parcelable {
    override fun describeContents(): Int {
        return 0 // En la mayoría de los casos, devolver cero es suficiente
    }

}
