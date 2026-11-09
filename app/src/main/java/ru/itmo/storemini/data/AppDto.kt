package ru.itmo.storemini.data

import kotlinx.serialization.Serializable

@Serializable
data class AppCatalogItemDto(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val iconUrl: String,
)

@Serializable
data class AppDetailsDto(
    val id: String,
    val name: String,
    val developer: String,
    val category: String,
    val ageRating: Int,
    val size: Float,
    val iconUrl: String,
    val screenshotUrlList: List<String>,
    val description: String,
)
