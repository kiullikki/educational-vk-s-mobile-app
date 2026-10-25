package ru.itmo.storemini.domain.appdetails

data class AppItem(
    val iconRes: Int,
    val name: String,
    val description: String,
    val fullDescription: String,
    val category: String,
    val id: Int,
    val isFavorite: Boolean,
    val developer: String,
    val ageRating: Int,
    val sizeMb: Float,
)

