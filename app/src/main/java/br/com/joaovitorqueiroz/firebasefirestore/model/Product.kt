package br.com.joaovitorqueiroz.firebasefirestore.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
     val id : String?,
     val nome : String,
     val valor : Long,
     val image : String
) : Parcelable
