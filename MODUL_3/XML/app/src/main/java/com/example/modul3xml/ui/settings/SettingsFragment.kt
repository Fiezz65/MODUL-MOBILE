package com.example.modul3xml.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.modul3xml.R
import com.example.modul3xml.databinding.FragmentSettingsBinding
import com.example.modul3xml.util.LanguageManager

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupLanguageButtons()
    }

    private fun setupToolbar() {
        binding.toolbarSettings.title = getString(R.string.title_settings)
        binding.toolbarSettings.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupLanguageButtons() {
        binding.buttonIndonesia.setOnClickListener {
            LanguageManager.setLocale(requireActivity(), "id")
            refreshTexts()
        }

        binding.buttonEnglish.setOnClickListener {
            LanguageManager.setLocale(requireActivity(), "en")
            refreshTexts()
        }
    }

    private fun refreshTexts() {
        binding.toolbarSettings.title = getString(R.string.title_settings)
        binding.buttonIndonesia.text = getString(R.string.btn_indonesia)
        binding.buttonEnglish.text = getString(R.string.btn_english)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}