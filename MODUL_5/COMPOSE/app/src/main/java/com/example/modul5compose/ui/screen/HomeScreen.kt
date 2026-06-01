package com.example.modul5compose.ui.screen

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.modul5compose.R
import com.example.modul5compose.data.NetworkResult
import com.example.modul5compose.model.Anime
import com.example.modul5compose.viewmodel.AnimeViewModel

@Composable
fun HomeScreen(navController: NavController, vm: AnimeViewModel) {
    val state by vm.animeState.collectAsState()
    val query by vm.searchQuery.collectAsState()
    val lang = LocalConfiguration.current.locales[0].toLanguageTag()
    val focus = LocalFocusManager.current
    val listState = rememberLazyListState()

    var lastData by remember { mutableStateOf<List<Anime>>(emptyList()) }
    var lastQ by remember { mutableStateOf("") }

    if (state is NetworkResult.Success) {
        val s = state as NetworkResult.Success
        lastData = s.data
        lastQ = s.query
    }

    LaunchedEffect(lastQ) {
        if (lastQ.isEmpty() && lastData.isNotEmpty()) listState.scrollToItem(0)
    }

    LaunchedEffect(lang) { vm.loadAnime(lang) }

    BackHandler(query.isNotEmpty()) {
        vm.onSearchQueryChanged("", lang)
        focus.clearFocus()
    }

    Scaffold(
        topBar = {
            Surface(color = Color.Black, shadowElevation = 4.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth().statusBarsPadding().height(64.dp).padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.app_name), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, null, tint = Color.White)
                    }
                }
            }
        }
    ) { p ->
        Column(Modifier.padding(p)) {
            OutlinedTextField(
                value = query,
                onValueChange = { vm.onSearchQueryChanged(it, lang) },
                modifier = Modifier.fillMaxWidth().padding(16.dp, 8.dp),
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { vm.onSearchQueryChanged("", lang); focus.clearFocus() }) {
                            Icon(Icons.Default.Close, null)
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Box(Modifier.weight(1f).fillMaxSize()) {
                if (lastData.isNotEmpty()) {
                    MainList(lastData, navController, lastQ.isNotEmpty(), listState)
                }
                if (state is NetworkResult.Loading && lastData.isEmpty()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center), color = Color.White)
                }
                if (state is NetworkResult.Error && lastData.isEmpty()) {
                    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                        Text((state as NetworkResult.Error).message)
                        Button(onClick = { vm.loadAnime(lang) }, Modifier.padding(top = 8.dp)) { Text("Coba Lagi") }
                    }
                }
            }
        }
    }
}

@Composable
fun MainList(data: List<Anime>, nav: NavController, isSearch: Boolean, state: LazyListState) {
    val ctx = LocalContext.current
    LazyColumn(state = state, modifier = Modifier.fillMaxSize()) {
        if (!isSearch) {
            item {
                Text(stringResource(R.string.title_highlight), Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(data.take(5)) { anime ->
                        Card(onClick = { nav.navigate("detail/${anime.id}") }, Modifier.size(210.dp, 115.dp)) {
                            AsyncImage(anime.posterUrl ?: anime.imageRes, null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
        items(data, key = { it.id }) { anime ->
            AnimeRow(anime, { nav.navigate("detail/${anime.id}") }, { ctx.startActivity(Intent(Intent.ACTION_VIEW, anime.url.toUri())) })
        }
    }
}

@Composable
fun AnimeRow(item: Anime, onDetail: () -> Unit, onWeb: () -> Unit) {
    val isIndo = LocalConfiguration.current.locales[0].language.startsWith("i")
    val description = (if (isIndo) item.plotId else item.plotEn)
        .takeIf { it.isNotBlank() }
        ?: stringResource(R.string.no_description_available)
    Card(
        Modifier.padding(16.dp, 8.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.padding(12.dp).height(145.dp)) {
            AsyncImage(item.posterUrl ?: item.imageRes, null, Modifier.width(95.dp).fillMaxHeight(), contentScale = ContentScale.Crop)
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(item.title, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${stringResource(R.string.label_release_date)}: ${item.releaseDate}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(
                        description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onWeb) { Text(stringResource(R.string.btn_browser), fontSize = 12.sp) }
                    Button(onDetail) { Text(stringResource(R.string.btn_detail), fontSize = 12.sp) }
                }
            }
        }
    }
}