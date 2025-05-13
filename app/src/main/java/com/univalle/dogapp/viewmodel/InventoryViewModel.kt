package com.univalle.dogapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.univalle.dogapp.model.Inventory
import com.univalle.dogapp.model.BreedsResponse
import com.univalle.dogapp.model.BreedImageResponse
import com.univalle.dogapp.repository.InventoryRepository
import kotlinx.coroutines.launch
import android.util.Log


class InventoryViewModel(application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val inventoryRepository = InventoryRepository(context)

    private val _listInventory = MutableLiveData<MutableList<Inventory>>()
    val listInventory: LiveData<MutableList<Inventory>> get() = _listInventory

    private val _progresState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progresState

    private val _sintomas = MutableLiveData<List<String>>()
    val sintomas: LiveData<List<String>> get() = _sintomas

    private val _breedsList = MutableLiveData<List<String>>()
    val breedsList: LiveData<List<String>> get() = _breedsList

    private val _breedImage = MutableLiveData<String>()
    val breedImage: LiveData<String> get() = _breedImage

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        cargarSintomas()
    }

    private fun cargarSintomas() {
        val sintomasList = listOf(
            "Sintomas","Solo duerme", "No come", "Fractura extremidad", "Tiene pulgas",
            "Tiene garrapatas", "Bota demasiado pelo"
        )
        _sintomas.value = sintomasList
        Log.d("InventoryViewModel", "Síntomas cargados: $sintomasList")
    }


    fun saveInventory(inventory: Inventory) {
        viewModelScope.launch {

            _progresState.value = true
            try {
                inventoryRepository.saveInventory(inventory)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun getListInventory() {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listInventory.value = inventoryRepository.getListInventory()
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }

        }
    }

    fun deleteInventory(inventory: Inventory) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                inventoryRepository.deleteInventory(inventory)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }

        }
    }

    fun updateInventory(inventory: Inventory) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                inventoryRepository.updateRepositoy(inventory)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun getBreeds() {
        if (_breedsList.value.isNullOrEmpty()) {
            viewModelScope.launch {
                try {
                    val response = inventoryRepository.getBreeds()
                    response?.let {
                        _breedsList.postValue(it.message.keys.toList())
                    } ?: run {
                        _error.postValue("No se pudo obtener la lista de razas")
                    }
                } catch (e: Exception) {
                    _error.postValue("Error al obtener razas: ${e.message}")
                }
            }
        }
    }

    suspend fun getBreedImage(breed: String): String? {
        return try {
            val response = inventoryRepository.getBreedImage(breed)
            if (response != null && response.status == "success") {
                response.message
            } else {
                _error.postValue("No se pudo obtener imagen para la raza")
                null
            }
        } catch (e: Exception) {
            _error.postValue("Error al obtener la imagen: ${e.message}")
            null
        }
    }
}

