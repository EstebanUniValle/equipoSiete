package com.univalle.dogapp.view.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.univalle.dogapp.R
import com.univalle.dogapp.databinding.FragmentHomeInventoryBinding
import com.univalle.dogapp.view.adapter.InventoryAdapter
import com.univalle.dogapp.viewmodel.InventoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeInventoryFragment : Fragment() {
    private lateinit var binding: FragmentHomeInventoryBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeInventoryBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controladores()
        observadorViewModel()

    }

    private fun controladores() {
        binding.fbagregar.setOnClickListener {
            findNavController().navigate(R.id.action_homeInventoryFragment_to_addItemFragment)
        }

    }

    private fun observadorViewModel(){
        observerListInventory()
        observerProgress()
    }

    private fun observerListInventory(){

        inventoryViewModel.getListInventory()
        inventoryViewModel.listInventory.observe(viewLifecycleOwner){ listInventory ->
            val recycler = binding.recyclerview
            val layoutManager =LinearLayoutManager(context)
            recycler.layoutManager = layoutManager
            val adapter = InventoryAdapter(listInventory, findNavController())
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()

        }

    }
    private fun observerProgress(){
        inventoryViewModel.progresState.observe(viewLifecycleOwner){status ->
            binding.progress.isVisible = status
        }
    }

}