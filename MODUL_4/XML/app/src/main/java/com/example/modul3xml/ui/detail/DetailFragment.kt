package com.example.modul3xml.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.modul3xml.R
import com.example.modul3xml.data.GameData
import com.example.modul3xml.databinding.FragmentDetailBinding
import com.example.modul3xml.util.LanguageManager
import timber.log.Timber

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.let { binding ->
            setupToolbar(binding)
            bindGameData(binding)
        }
    }

    private fun setupToolbar(binding: FragmentDetailBinding) {
        binding.toolbarDetail.setNavigationOnClickListener {
            val gameId = arguments?.getInt("gameId") ?: -1
            Timber.d("Kembali dari halaman Detail untuk game ID: $gameId")
            findNavController().popBackStack()
        }
    }

    private fun bindGameData(binding: FragmentDetailBinding) {
        val gameId = arguments?.getInt("gameId") ?: -1
        val game = GameData.findGameById(gameId)

        if (game == null) {
            Timber.w("Game dengan id=$gameId tidak ditemukan!")
            binding.toolbarDetail.title = getString(R.string.title_detail)
            return
        }

        Timber.d("Menampilkan detail game: ${game.title} (ID: ${game.id})")

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
