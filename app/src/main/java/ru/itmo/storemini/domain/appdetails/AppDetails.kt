package ru.itmo.storemini.domain.appdetails

data class AppDetails(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val iconUrl: String,
    val isFavorite: Boolean,
    val developer: String,
    val ageRating: Int,
    val sizeMb: Float,
    val screenshotUrls: List<String>,
)
