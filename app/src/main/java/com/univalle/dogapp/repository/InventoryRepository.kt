package com.univalle.dogapp.repository
import android.content.Context
import com.univalle.dogapp.data.InventoryDB
import com.univalle.dogapp.data.InventoryDao
import com.univalle.dogapp.model.Inventory
import com.univalle.dogapp.model.ProductModelResponse
import com.univalle.dogapp.webservice.ApiService
import com.univalle.dogapp.webservice.ApiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    suspend fun getProducts(): MutableList<ProductModelResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProducts()
                response
            } catch (e: Exception) {

                e.printStackTrace()
                mutableListOf()
            }
        }
    }
}