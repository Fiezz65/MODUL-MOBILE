package com.example.modul5compose.ui.screen

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.modul5compose.R
import com.example.modul5compose.data.NetworkResult
import com.example.modul5compose.model.Anime
import com.example.modul5compose.viewmodel.AnimeViewModel

@Composable
fun HomeScreen(
    navController: androidx.navigation.NavController,
    viewModel: AnimeViewModel
) {
    val state by viewModel.animeState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val configuration = LocalConfiguration.current
    val currentLang = configuration.locales[0].toLanguageTag()
    val focusManager = LocalFocusManager.current

    // Jika sedang mencari, tekan Back akan menghapus pencarian, bukan keluar aplikasi
    BackHandler(enabled = searchQuery.isNotEmpty()) {
        viewModel.onSearchQueryChanged("", currentLang)
        focusManager.clearFocus()
    }

    androidx.compose.runtime.LaunchedEffect(currentLang) {
        viewModel.loadAnime(currentLang, searchQuery)
    }

    Scaffold(
        topBar = {
            Surface(color = Color.Black, shadowElevation = 4.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(64.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, null, tint = Color.White)
                    }
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it, currentLang) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search anime...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { 
                            viewModel.onSearchQueryChanged("", currentLang)
                            focusManager.clearFocus()
                        }) {
                            Icon(Icons.Default.Close, "Clear", tint = Color.Gray)
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.DarkGray,
                    focusedBorderColor = Color(0xFF2196F3)
                ),
                singleLine = true
            )

            when (state) {
                NetworkResult.Loading -> LoadingContent()
                is NetworkResult.Error -> ErrorContent(
                    message = (state as NetworkResult.Error).message,
                    onRetry = { viewModel.loadAnime(currentLang, searchQuery) }
                )
                is NetworkResult.Success -> {
                    val animes = (state as NetworkResult.Success<List<Anime>>).data
                    AnimeContent(
                        animes = animes,
                        navController = navController,
                        showHighlights = searchQuery.isBlank()
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message)
        Spacer(Modifier.height(12.dp))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.btn_detail))
        }
    }
}

@Composable
private fun AnimeContent(
    animes: List<Anime>,
    navController: androidx.navigation.NavController,
    showHighlights: Boolean
) {
    val context = LocalContext.current

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (showHighlights && animes.isNotEmpty()) {
            item {
                Text(
                    stringResource(R.string.title_highlight),
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.SemiBold
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(animes.take(5)) { anime ->
                        Card(
                            onClick = { navController.navigate("detail/${anime.id}") },
                            modifier = Modifier.size(200.dp, 120.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            AsyncImage(
                                model = anime.posterUrl ?: anime.imageRes,
                                contentDescription = anime.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }

        items(animes) { anime ->
            AnimeListItem(
                anime = anime,
                onDetail = { navController.navigate("detail/${anime.id}") },
                onBrowser = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, anime.url.toUri()))
                }
            )
        }
    }
}

@Composable
fun AnimeListItem(anime: Anime, onDetail: () -> Unit, onBrowser: () -> Unit) {
    val lang = LocalConfiguration.current.locales[0].language
    val isId = lang == "in" || lang == "id"

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(175.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Row(modifier = Modifier.padding(12.dp).fillMaxSize()) {
            AsyncImage(
                model = anime.posterUrl ?: anime.imageRes,
                contentDescription = anime.title,
                modifier = Modifier.size(100.dp, 150.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(start = 12.dp).fillMaxHeight()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        anime.title,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(anime.year, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Text(
                    text = if (isId) anime.plotId else anime.plotEn,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray,
                    maxLines = 3, // Sedikit lebih panjang untuk sinopsis
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp),
                    lineHeight = 16.sp
                )
                Spacer(Modifier.weight(1f))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    val blue = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                    Button(onClick = onBrowser, colors = blue, modifier = Modifier.height(36.dp)) {
                        Text(stringResource(R.string.btn_browser), fontSize = 11.sp)
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = onDetail, colors = blue, modifier = Modifier.height(36.dp)) {
                        Text(stringResource(R.string.btn_detail), fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
