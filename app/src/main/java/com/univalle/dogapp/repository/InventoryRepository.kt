package com.univalle.dogapp.repository
import android.content.Context
import com.univalle.dogapp.data.InventoryDB
import com.univalle.dogapp.data.InventoryDao
import com.univalle.dogapp.model.Inventory
import com.univalle.dogapp.model.BreedsResponse
import com.univalle.dogapp.model.BreedImageResponse
import com.univalle.dogapp.webservice.ApiService
import com.univalle.dogapp.webservice.ApiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log

class InventoryRepository(val context: Context){
    private var inventoryDao:InventoryDao = InventoryDB.getDatabase(context).inventoryDao()
    private var apiService: ApiService = ApiUtils.getApiService()
     suspend fun saveInventory(inventory:Inventory){
         withContext(Dispatchers.IO){
             inventoryDao.saveInventory(inventory)
         }
     }

    suspend fun getListInventory():MutableList<Inventory>{
        return withContext(Dispatchers.IO){
            inventoryDao.getListInventory()
        }
    }

    suspend fun deleteInventory(inventory: Inventory){
        withContext(Dispatchers.IO){
            inventoryDao.deleteInventory(inventory)
        }
    }

    suspend fun updateRepositoy(inventory: Inventory){
        withContext(Dispatchers.IO){
            inventoryDao.updateInventory(inventory)
        }
    }

    // Obtener razas de perros desde la API
    suspend fun getBreeds(): BreedsResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getBreeds()
                if (response != null && response.message.isNotEmpty()) {
                    Log.d("InventoryRepository", "Razas obtenidas correctamente")
                    return@withContext response
                } else {
                    Log.e("InventoryRepository", "Respuesta vacía o nula de las razas")
                    return@withContext null
                }
            } catch (e: Exception) {
                Log.e("InventoryRepository", "Error al obtener razas: ${e.message}", e)
                return@withContext null
            }
        }
    }

    // Obtener imagen de una raza desde la API
    suspend fun getBreedImage(breed: String): BreedImageResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getBreedImage(breed)
                if (response != null) {
                    Log.d("InventoryRepository", "Imagen obtenida para la raza $breed")
                    return@withContext response
                } else {
                    Log.e("InventoryRepository", "Respuesta vacía de imagen para la raza $breed")
                    return@withContext null
                }
            } catch (e: Exception) {
                Log.e("InventoryRepository", "Error al obtener imagen para la raza $breed: ${e.message}", e)
                return@withContext null
            }
        }
    }
}