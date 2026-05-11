package com.example.modul3xml.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.modul3xml.R
import com.example.modul3xml.databinding.FragmentSettingsBinding
import com.example.modul3xml.util.LanguageManager

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // Menghindari penggunaan !! dengan tidak menggunakan property binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Gunakan safe call .let untuk memastikan binding tidak null
        _binding?.let { binding ->
            setupToolbar(binding)
            setupLanguageButtons(binding)
        }
    }

    private fun setupToolbar(binding: FragmentSettingsBinding) {
        binding.toolbarSettings.title = getString(R.string.title_settings)
        binding.toolbarSettings.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupLanguageButtons(binding: FragmentSettingsBinding) {
        binding.buttonIndonesia.setOnClickListener {
            LanguageManager.setLocale(requireActivity(), "id")
            refreshTexts(binding)
        }

        binding.buttonEnglish.setOnClickListener {
            LanguageManager.setLocale(requireActivity(), "en")
            refreshTexts(binding)
        }
    }

    private fun refreshTexts(binding: FragmentSettingsBinding) {
        binding.toolbarSettings.title = getString(R.string.title_settings)
        binding.buttonIndonesia.text = getString(R.string.btn_indonesia)
        binding.buttonEnglish.text = getString(R.string.btn_english)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
