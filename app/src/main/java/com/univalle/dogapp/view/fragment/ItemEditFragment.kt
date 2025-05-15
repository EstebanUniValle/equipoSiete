package com.univalle.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.univalle.dogapp.R
import com.univalle.dogapp.databinding.FragmentItemEditBinding
import com.univalle.dogapp.model.Inventory
import com.univalle.dogapp.viewmodel.InventoryViewModel
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class ItemEditFragment : Fragment() {
    private lateinit var binding: FragmentItemEditBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()
    private lateinit var receivedInventory: Inventory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemEditBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inventoryViewModel.getBreeds()
        dataInventory()
        controladores()
        observerViewModel()
    }

    private fun validarCampos() {
        val name = binding.edPetName.text.toString()
        val breed = binding.edPetBreed.text.toString()
        val owner = binding.edOwnerName.text.toString()
        val phone = binding.edPhone.text.toString()

        val camposLlenos = name.isNotEmpty() && breed.isNotEmpty() && owner.isNotEmpty() && phone.isNotEmpty()

        val hayCambios = name != receivedInventory.name ||
                breed != receivedInventory.breed ||
                owner != receivedInventory.owner ||
                phone != receivedInventory.phone

        binding.btnEditInventory.isEnabled = camposLlenos && hayCambios
    }

    private fun observerViewModel() {

        inventoryViewModel.breedsList.observe(viewLifecycleOwner) { breeds ->
            breeds?.let {
                configurarAutoCompleteBreeds(it)
            }
        }
    }

    private fun actualizarImagenPorRaza(breed: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val imageUrl = inventoryViewModel.getBreedImage(breed)

                val imageToSave = if (!imageUrl.isNullOrEmpty()) {
                    imageUrl
                } else {
                    "android.resource://${requireContext().packageName}/drawable/imagen_por_defecto"
                }

                receivedInventory = receivedInventory.copy(imagen = imageToSave)


            } catch (e: Exception) {
                // Log o Toast si quieres notificar error
            }
        }
    }


    private fun controladores(){
        binding.edPetName.doAfterTextChanged { validarCampos() }
        binding.edPetBreed.doAfterTextChanged { validarCampos() }
        binding.edOwnerName.doAfterTextChanged { validarCampos() }
        binding.edPhone.doAfterTextChanged { validarCampos() }
        binding.edPetBreed.setOnItemClickListener { parent, _, position, _ ->
            val selectedBreed = parent.getItemAtPosition(position).toString()
            actualizarImagenPorRaza(selectedBreed)
            validarCampos()
        }



        binding.btnEditInventory.setOnClickListener {
            updateInventory()
        }
    }




    private fun dataInventory(){
        val receivedBundle = arguments
        receivedInventory = receivedBundle?.getSerializable("dataInventory") as Inventory
        binding.edPetName.setText(receivedInventory.name)
        binding.edPetBreed.setText(receivedInventory.breed)
        binding.edOwnerName.setText(receivedInventory.owner)
        binding.edPhone.setText(receivedInventory.phone)

        validarCampos()
    }

    private fun updateInventory(){
        val name = binding.edPetName.text.toString()
        val breed = binding.edPetBreed.text.toString()
        val owner = binding.edOwnerName.text.toString()
        val phone = binding.edPhone.text.toString()

        val inventory = Inventory(receivedInventory.id, name,breed,owner,phone, receivedInventory.symptom, receivedInventory.imagen)
        inventoryViewModel.updateInventory(inventory)
        findNavController().navigate(R.id.action_itemEditFragment_to_homeInventoryFragment)

    }

    private fun configurarAutoCompleteBreeds(breeds: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            breeds
        )

        binding.edPetBreed.apply {
            setAdapter(adapter)
            setOnClickListener { showDropDown() }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showDropDown()
            }
        }
    }
}