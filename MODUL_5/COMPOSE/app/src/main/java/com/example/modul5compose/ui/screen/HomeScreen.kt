package com.example.modul5compose.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
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
    val configuration = LocalConfiguration.current
    val currentLang = configuration.locales[0].toLanguageTag()

    androidx.compose.runtime.LaunchedEffect(currentLang) {
        viewModel.loadAnime(currentLang)
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
        when (state) {
            NetworkResult.Loading -> LoadingContent(padding)
            is NetworkResult.Error -> ErrorContent(
                padding = padding,
                message = (state as NetworkResult.Error).message,
                onRetry = { 
                    val lang = configuration.locales[0].toLanguageTag()
                    viewModel.loadAnime(lang) 
                }
            )
            is NetworkResult.Success -> {
                val animes = (state as NetworkResult.Success<List<Anime>>).data
                AnimeContent(
                    padding = padding,
                    animes = animes,
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    padding: PaddingValues,
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
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
    padding: PaddingValues,
    animes: List<Anime>,
    navController: androidx.navigation.NavController
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding)
    ) {
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
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize()
        ) {
            AsyncImage(
                model = anime.posterUrl ?: anime.imageRes,
                contentDescription = anime.title,
                modifier = Modifier.size(100.dp, 150.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxHeight()
            ) {
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
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp),
                    lineHeight = 16.sp
                )
                Spacer(Modifier.weight(1f))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    val blue = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                    Button(
                        onClick = onBrowser,
                        colors = blue,
                        modifier = Modifier.height(36.dp)
                    ) {
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