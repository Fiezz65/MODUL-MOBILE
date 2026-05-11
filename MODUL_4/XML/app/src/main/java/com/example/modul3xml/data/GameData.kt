package com.example.modul3xml.data

import com.example.modul3xml.R

object GameData {

    val gameList = listOf(
        Game(
            id = 1,
            title = "Honkai: Star Rail",
            year = "2022",
            plotId = "Perjalanan melintasi bintang-bintang di atas Astral Express.",
            plotEn = "A journey across the stars aboard the Astral Express.",
            imageRes = R.drawable.img_hsr,
            url = "https://hsr.hoyoverse.com/"
        ),
        Game(
            id = 2,
            title = "Arknights: Endfield",
            year = "2026",
            plotId = "Eksplorasi planet Talos-II yang berbahaya dan penuh misteri.",
            plotEn = "Explore the dangerous and mysterious planet of Talos-II.",
            imageRes = R.drawable.img_endfield,
            url = "https://endfield.gryphline.com/en-us"
        ),
        Game(
            id = 3,
            title = "Clair Obscur: Expedition 33",
            year = "2025",
            plotId = "Misi terakhir untuk menghancurkan entitas yang menghapus umat manusia.",
            plotEn = "The final mission to destroy an entity erasing humanity.",
            imageRes = R.drawable.img_coe33,
            url = "https://store.steampowered.com/app/1903340/Clair_Obscur_Expedition_33/"
        ),
        Game(
            id = 4,
            title = "Persona 5 Royal",
            year = "2019",
            plotId = "Kisah pencuri hati (Phantom Thieves) yang mengubah masyarakat.",
            plotEn = "The story of Phantom Thieves changing corrupt society.",
            imageRes = R.drawable.img_p5r,
            url = "https://store.steampowered.com/app/1687950/Persona_5_Royal/"
        ),
        Game(
            id = 5,
            title = "Persona 3 Reload",
            year = "2024",
            plotId = "Mengungkap misteri Dark Hour dan menyelamatkan dunia.",
            plotEn = "Uncover the mystery of the Dark Hour and save the world.",
            imageRes = R.drawable.img_p3r,
            url = "https://store.steampowered.com/app/2161700/Persona_3_Reload/"
        )
    )

    fun findGameById(gameId: Int): Game? {
        return gameList.find { it.id == gameId }
    }
}