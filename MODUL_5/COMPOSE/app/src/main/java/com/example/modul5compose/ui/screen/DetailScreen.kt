package com.example.modul5compose.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modul5compose.viewmodel.AnimeViewModel
import com.example.modul5compose.viewmodel.AnimeViewModelFactory

@Composable
fun DetailScreen(
    navController: androidx.navigation.NavController,
    animeId: Int?
) {
    val viewModel: AnimeViewModel = viewModel(factory = AnimeViewModelFactory("Hafiz Perdana"))
    val animes by viewModel.animes.collectAsState()
    val anime = animeId?.let { id -> animes.firstOrNull { it.id == id } }
    val lang = LocalConfiguration.current.locales.get(0).language
    val overviewLabel = if (lang == "in" || lang == "id") "Sinopsis" else "Overview"

    Scaffold(
        topBar = {
            Surface(color = Color.Black, shadowElevation = 4.dp) {
                Row(Modifier.fillMaxWidth().statusBarsPadding().height(64.dp).padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                    Text(
                        text = anime?.title ?: "Detail",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    ) { p ->
        anime?.let {
            Column(Modifier.fillMaxSize().padding(p).verticalScroll(rememberScrollState()).padding(16.dp)) {
                Surface(
                    modifier = Modifier.fillMaxWidth().height(260.dp).clip(RoundedCornerShape(16.dp)),
                    color = Color(0xFF1A1A1A)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = it.imageRes),
                            contentDescription = it.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Row(Modifier.fillMaxWidth().padding(vertical = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(it.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text(it.year, style = MaterialTheme.typography.titleLarge, color = Color.Gray)
                }

                Text(text = overviewLabel, fontWeight = FontWeight.Bold)
                val overviewText = if (lang == "in" || lang == "id") it.plotId else it.plotEn
                Text(overviewText, modifier = Modifier.padding(top = 8.dp))

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}