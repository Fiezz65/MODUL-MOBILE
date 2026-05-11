package com.example.modul3xml.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.modul3xml.R
import com.example.modul3xml.databinding.FragmentHomeBinding
import com.example.modul3xml.ui.viewmodel.GameViewModel
import com.example.modul3xml.ui.viewmodel.GameViewModelFactory
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val viewModel: GameViewModel by viewModels {
        GameViewModelFactory("All Games")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.let { binding ->
            setupMenu(binding)
            setupRecyclerViews(binding)
            observeViewModel(binding)
        }
    }

    private fun setupMenu(binding: FragmentHomeBinding) {
        binding.toolbarHome.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_settings -> {
                    findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                    true
                }
                else -> false
            }
        }
        binding.toolbarHome.title = getString(R.string.app_name)
    }

    private fun setupRecyclerViews(binding: FragmentHomeBinding) {
        binding.rvHighlight.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvGames.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel(binding: FragmentHomeBinding) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.gameList.collect { games ->
                        if (games.isNotEmpty()) {
                            binding.rvHighlight.adapter = HighlightAdapter(games) { game ->
                                viewModel.onDetailClicked(game)
                            }
                            binding.rvGames.adapter = GameListAdapter(
                                games = games,
                                onBrowserClick = { game ->
                                    viewModel.onExplicitIntentClicked(game)
                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(game.url)))
                                },
                                onDetailClick = { game ->
                                    viewModel.onDetailClicked(game)
                                }
                            )
                        }
                    }
                }

                launch {
                    viewModel.navigateToDetail.collect { game ->
                        game?.let {
                            val bundle = Bundle().apply {
                                putInt("gameId", it.id)
                            }
                            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
                            viewModel.onDetailNavigated()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        _binding?.let { binding ->
            binding.toolbarHome.title = getString(R.string.app_name)
        }
    }
}
