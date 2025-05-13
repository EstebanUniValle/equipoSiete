package com.univalle.dogapp.view.viewholder

import android.os.Bundle
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.univalle.dogapp.R
import com.univalle.dogapp.databinding.ItemInventoryBinding
import com.univalle.dogapp.model.Inventory

class InventoryViewHolder(
    private val binding: ItemInventoryBinding,
    private val navController: NavController
) : RecyclerView.ViewHolder(binding.root) {

    fun setItemInventory(inventory: Inventory) {
        binding.tvPetName.text = inventory.name // Nombre de la mascota
        binding.tvSymptom.text = inventory.symptom // Síntoma
        binding.tvid.text = "#${inventory.id}" // ID

        // Cargar la imagen usando Glide (asegúrate de agregar Glide en tus dependencias)
        Glide.with(binding.imageCard.context)
            .load(inventory.imagen) // Asume que tienes un campo de imagen en Inventory
            .centerCrop()
            .into(binding.imageCard)

        // Cuando se haga clic en el CardView, se navegará a otro fragmento
        binding.cvInventory.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("inventory", inventory)
            navController.navigate(R.id.action_homeInventoryFragment_to_itemDetailsFragment, bundle)
        }
    }
}