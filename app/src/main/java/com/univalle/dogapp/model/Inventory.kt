package com.univalle.dogapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "inventory")
data class Inventory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val breed: String,
    val owner: String,
    val phone: String,
    val symptom: String,
    val imagen: String? = null
) : Serializable
