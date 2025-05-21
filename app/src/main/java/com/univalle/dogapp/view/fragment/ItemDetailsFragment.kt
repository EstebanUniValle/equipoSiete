package com.univalle.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.univalle.dogapp.R
import com.univalle.dogapp.databinding.FragmentItemDetailsBinding
import com.univalle.dogapp.model.Inventory
import com.univalle.dogapp.viewmodel.InventoryViewModel

class ItemDetailsFragment : Fragment() {
    private lateinit var binding: FragmentItemDetailsBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()
    private lateinit var receivedInventory: Inventory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInventory()
        controladores()
        configurarRetroceso()
    }

    private fun configurarRetroceso() {
        binding.toolbarAdd.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun controladores() {
        binding.fbDelete.setOnClickListener {
            deleteInventory()
        }

        binding.fbEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("dataInventory", receivedInventory)
            findNavController().navigate(R.id.action_itemDetailsFragment_to_itemEditFragment, bundle)
        }
    }

    private fun dataInventory() {
        val receivedBundle = arguments
        receivedInventory = receivedBundle?.getSerializable("clave") as Inventory
        binding.tvId.text = "#${receivedInventory.id.toString()}"
        binding.toolbarTitle.text = receivedInventory.name
        binding.tvBreed.text = receivedInventory.breed
        binding.tvCondition.text = receivedInventory.symptom
        binding.tvOwner.text = "Propietario: ${receivedInventory.owner}"
        binding.tvPhone.text = "Teléfono ${receivedInventory.phone}"
        Glide.with(binding.imagePet.context)
            .load(receivedInventory.imagen)
            .centerCrop()
            .into(binding.imagePet)
    }

    private fun deleteInventory(){
        inventoryViewModel.deleteInventory(receivedInventory)
        inventoryViewModel.getListInventory()
        findNavController().popBackStack()
    }

}