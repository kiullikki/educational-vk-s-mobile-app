package ru.itmo.storemini.domain.catalog

data class AppSummary(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val iconUrl: String,
    val isFavorite: Boolean,
)
