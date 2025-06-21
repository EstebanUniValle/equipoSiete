package com.univalle.dogapp.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.univalle.dogapp.model.Inventory
import kotlinx.coroutines.tasks.await

class FirebaseInventoryDao {
    private val db = FirebaseFirestore.getInstance()
    private val inventoryCollection = db.collection("inventory")

    suspend fun saveInventory(inventory: Inventory) {
        try {
            val metadataRef = db.collection("metadata").document("inventoryCounter")

            db.runTransaction { transaction ->
                val snapshot = transaction.get(metadataRef)
                val currentCount = snapshot.getLong("count") ?: 0
                val newCount = currentCount + 1

                // Crear nuevo documento con ID incremental
                val newInventory = inventory.copy(id = newCount.toInt())

                val newDocRef = inventoryCollection.document(newCount.toString())
                transaction.set(newDocRef, newInventory)

                // Actualizar contador
                transaction.update(metadataRef, "count", newCount)
            }.await()
        } catch (e: Exception) {
            Log.e("FirebaseInventoryDao", "Error al guardar inventario incremental: ${e.message}")
        }
    }



    suspend fun getListInventory(): MutableList<Inventory> {
        return try {
            val snapshot = inventoryCollection.get().await()
            val list = mutableListOf<Inventory>()
            for (doc in snapshot.documents) {
                try {
                    val data = doc.data ?: continue
                    val inventory = Inventory(
                        id = (data["id"] as? Number)?.toInt() ?: 0,
                        name = data["name"] as? String ?: "",
                        breed = data["breed"] as? String ?: "",
                        owner = data["owner"] as? String ?: "",
                        phone = data["phone"] as? String ?: "",
                        symptom = data["symptom"] as? String ?: "",
                        imagen = data["imagen"] as? String
                    )
                    list.add(inventory)
                } catch (e: Exception) {
                    Log.e("FirebaseInventoryDao", "Error al mapear documento ${doc.id}: ${e.message}")
                }
            }
            list
        } catch (e: Exception) {
            Log.e("FirebaseInventoryDao", "Error al obtener lista: ${e.message}")
            mutableListOf()
        }
    }

    suspend fun deleteInventory(inventory: Inventory) {
        try {
            Log.d("FirebaseInventoryDao", "Intentando eliminar documento con ID: ${inventory.id}")
            inventoryCollection.whereEqualTo("id", inventory.id).get().await().documents.firstOrNull()?.reference?.delete()?.await()
        } catch (e: Exception) {
            Log.e("FirebaseInventoryDao", "Error al eliminar: ${e.message}")
        }
    }

    suspend fun updateInventory(inventory: Inventory) {
        try {
            val id = inventory.id.toString()
            if (id.isBlank() || id == "0") {
                Log.e("FirebaseInventoryDao", "ID inválido para actualizar: $id")
                return
            }
            inventoryCollection.document(id).set(inventory).await()
            Log.d("FirebaseInventoryDao", "Inventario actualizado con éxito: $id")
        } catch (e: Exception) {
            Log.e("FirebaseInventoryDao", "Error al actualizar: ${e.message}")
        }
    }

}
