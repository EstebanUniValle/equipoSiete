package com.univalle.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import android.widget.ArrayAdapter
import com.univalle.dogapp.databinding.FragmentAddItemBinding
import com.univalle.dogapp.model.Inventory
import com.univalle.dogapp.viewmodel.InventoryViewModel
import android.text.InputType
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.util.Log

class AddItemFragment : Fragment() {

    private lateinit var binding: FragmentAddItemBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddItemBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controladores()
        observerViewModel()
        inventoryViewModel.getBreeds()
        configurarRetroceso()
    }

    private fun controladores() {
        validarDatos()
        binding.btnSaveInventory.setOnClickListener {
            saveInventory()
        }
    }

    private fun configurarRetroceso() {
        binding.toolbarAdd.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun saveInventory() {
        val name = binding.etPetName.text.toString()
        val breed = binding.etPetBreed.text.toString()
        val owner = binding.etOwnerName.text.toString()
        val phone = binding.etPhone.text.toString()
        val symptom = binding.autoCompleteSintomas.text.toString()

        // Validación simple
        if (symptom.isEmpty()) {
            Toast.makeText(requireContext(), "Selecciona un síntoma", Toast.LENGTH_SHORT).show()
            return
        }

        // Llamada asincrónica para obtener la imagen
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val imageUrl = inventoryViewModel.getBreedImage(breed)
                if (!imageUrl.isNullOrEmpty()) {
                    val inventory = Inventory(name = name, breed = breed, owner = owner, phone = phone, symptom = symptom, imagen = imageUrl)
                    inventoryViewModel.saveInventory(inventory)
                    Toast.makeText(context, "Artículo guardado!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), "No se pudo obtener la imagen", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al guardar el artículo", Toast.LENGTH_SHORT).show()
                Log.e("AddItemFragment", "Error: ${e.message}")
            }
        }
    }


    private fun validarDatos() {
        val listEditText = listOf(
            binding.etPetName,
            binding.etPetBreed,
            binding.etOwnerName,
            binding.etPhone
        )

        for (editText in listEditText) {
            editText.addTextChangedListener {
                val isListFull = listEditText.all { it.text?.isNotEmpty() == true }
                binding.btnSaveInventory.isEnabled = isListFull
            }
        }
    }

    private fun observerViewModel() {
        inventoryViewModel.sintomas.observe(viewLifecycleOwner) { sintomas ->
            sintomas?.let {
                configurarAutoCompleteSintomas(it)
            }
        }

        inventoryViewModel.breedsList.observe(viewLifecycleOwner) { breeds ->
            breeds?.let {
                configurarAutoCompleteBreeds(it)
            }
        }
    }

    private fun configurarAutoCompleteSintomas(sintomas: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            sintomas
        )

        binding.autoCompleteSintomas.apply {
            setAdapter(adapter)
            inputType = InputType.TYPE_NULL
            keyListener = null
            setOnClickListener { showDropDown() }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showDropDown()
            }
        }
    }

    private fun configurarAutoCompleteBreeds(breeds: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            breeds
        )

        binding.etPetBreed.apply {
            setAdapter(adapter)
            setOnClickListener { showDropDown() }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showDropDown()
            }
        }
    }
}


