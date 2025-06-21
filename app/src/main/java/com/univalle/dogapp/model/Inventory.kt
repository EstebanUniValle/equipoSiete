package com.univalle.dogapp.model

import java.io.Serializable

data class Inventory(
    val id: Int = 0,
    val name: String = "",
    val breed: String = "",
    val owner: String = "",
    val phone: String = "",
    val symptom: String = "",
    val imagen: String? = null
) : Serializable
