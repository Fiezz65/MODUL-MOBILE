package com.example.modul3xml.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.modul3xml.R
import com.example.modul3xml.data.GameData
import com.example.modul3xml.databinding.FragmentDetailBinding
import com.example.modul3xml.util.LanguageManager

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDetailBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        bindGameData()
    }

    private fun setupToolbar() {
        binding.toolbarDetail.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindGameData() {
        val gameId = arguments?.getInt("gameId") ?: -1
        val game = GameData.findGameById(gameId)

        if (game == null) {
            binding.toolbarDetail.title = getString(R.string.title_detail)
            return
        }

        val isIndonesian = LanguageManager.isIndonesian(requireContext())

        binding.toolbarDetail.title = game.title
        binding.imageDetail.setImageResource(game.imageRes)
        binding.textDetailTitle.text = game.title
        binding.textDetailYear.text = game.year
        binding.textDetailPlot.text = if (isIndonesian) game.plotId else game.plotEn
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}