package com.univalle.dogapp.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.univalle.dogapp.R
import com.univalle.dogapp.databinding.FragmentLoginBinding
import com.univalle.dogapp.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)

        observerIsRegister()
        checkSession()

        binding.loginButton.setOnClickListener {
            loginUser()
        }

        binding.registerText.setOnClickListener {
            registerUser()
        }
    }

    private fun loginUser() {
        val email = binding.emailInput.text.toString()
        val pass = binding.passwordInput.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            loginViewModel.loginUser(email, pass) { isLogin ->
                if (isLogin) {
                    sharedPreferences.edit().putString("email", email).apply()
                    goToHome()
                } else {
                    Toast.makeText(requireContext(), "Login incorrecto", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Campos vacíos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerUser() {
        val email = binding.emailInput.text.toString()
        val pass = binding.passwordInput.text.toString()
        if (email.isNotEmpty() && pass.isNotEmpty()) {
            loginViewModel.registerUser(com.univalle.dogapp.model.UserRequest(email, pass))
        } else {
            Toast.makeText(requireContext(), "Campos vacíos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observerIsRegister() {
        loginViewModel.isRegister.observe(viewLifecycleOwner) { userResponse ->
            if (userResponse.isRegister) {
                Toast.makeText(requireContext(), userResponse.message, Toast.LENGTH_SHORT).show()
                sharedPreferences.edit().putString("email", userResponse.email).apply()
                goToHome()
            } else {
                Toast.makeText(requireContext(), userResponse.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkSession() {
        val email = sharedPreferences.getString("email", null)
        loginViewModel.sesion(email) { isEnableView ->
            if (isEnableView) {
                goToHome()
            }
        }
    }

    private fun goToHome() {
        findNavController().navigate(
            R.id.action_login_to_homeInventoryFragment,
            null,
            NavOptions.Builder()
                .setPopUpTo(R.id.login, true)
                .build()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
