package com.example.modul5compose.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.ui.res.stringResource
import com.example.modul5compose.R
import com.example.modul5compose.viewmodel.AnimeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, vm: AnimeViewModel, id: Int?) {
    val anime = id?.let { vm.animeById(it) }
    val lang = LocalConfiguration.current.locales[0].language
    val fallbackOverview = stringResource(R.string.no_description_available)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(anime?.title ?: "Detail", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { p ->
        if (anime != null) {
            Column(Modifier.padding(p).fillMaxSize().verticalScroll(rememberScrollState())) {
                AsyncImage(
                    model = anime.posterUrl ?: anime.imageRes,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(400.dp),
                    contentScale = ContentScale.Crop
                )
                Column(Modifier.padding(20.dp)) {
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Text(anime.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text(anime.year, style = MaterialTheme.typography.titleLarge, color = Color.Gray)
                    }
                    Text("${stringResource(R.string.label_release_date)}: ${anime.releaseDate}", Modifier.padding(vertical = 8.dp), color = Color.Gray)
                    Text(stringResource(R.string.label_overview), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
                    val overview = (if (lang.startsWith("i")) anime.plotId else anime.plotEn)
                        .takeIf { it.isNotBlank() }
                        ?: fallbackOverview
                    Text(overview, Modifier.padding(top = 8.dp))
                }
            }
        } else {
            Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
                Text("Data tidak ditemukan")
            }
        }
    }
}