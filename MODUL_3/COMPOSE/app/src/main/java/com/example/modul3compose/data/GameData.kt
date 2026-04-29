package com.example.modul3compose.data

import com.example.modul3compose.R
import com.example.modul3compose.model.Game

object GameData {
    val gameList = listOf(
        Game(
            1,
            "Honkai: Star Rail",
            "2022",
            "Perjalanan melintasi bintang-bintang di atas Astral Express.",
            "A journey across the stars aboard the Astral Express.",
            R.drawable.img_hsr,
            "https://hsr.hoyoverse.com/"),
        Game(
            2,
            "Arknights: Endfield",
            "2026",
            "Eksplorasi planet Talos-II yang berbahaya dan penuh misteri.",
            "Explore the dangerous and mysterious planet of Talos-II.",
            R.drawable.img_endfield,
            "https://endfield.gryphline.com/en-us"),
        Game(
            3,
            "Clair Obscur: Expedition 33",
            "2025",
            "Misi terakhir untuk menghancurkan entitas yang menghapus umat manusia.",
            "The final mission to destroy an entity erasing humanity.",
            R.drawable.img_coe33,
            "https://store.steampowered.com/app/1903340/Clair_Obscur_Expedition_33/"),
        Game(
            4,
            "Persona 5 Royal",
            "2019",
            "Kisah pencuri hati (Phantom Thieves) yang mengubah masyarakat.",
            "The story of Phantom Thieves changing corrupt society.",
            R.drawable.img_p5r,
            "https://store.steampowered.com/app/1687950/Persona_5_Royal/"),
        Game(
            5,
            "Persona 3 Reload",
            "2024",
            "Mengungkap misteri Dark Hour dan menyelamatkan dunia.",
            "Uncover the mystery of the Dark Hour and save the world.",
            R.drawable.img_p3r,
            "https://store.steampowered.com/app/2161700/Persona_3_Reload/")
    )
}