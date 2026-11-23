package ru.itmo.storemini

import ru.itmo.storemini.data.AppCatalogItemDto
import ru.itmo.storemini.data.AppDetailsDto
import ru.itmo.storemini.domain.appdetails.AppDetails
import ru.itmo.storemini.domain.catalog.AppSummary

fun catalogItemDto(id: String, name: String = "App $id") = AppCatalogItemDto(
    id = id,
    name = name,
    description = "Description $id",
    category = "Category $id",
    iconUrl = "https://example.com/$id.png",
)

fun appDetailsDto(id: String, name: String = "App $id") = AppDetailsDto(
    id = id,
    name = name,
    developer = "Developer $id",
    category = "Category $id",
    ageRating = 12,
    size = 42.5f,
    iconUrl = "https://example.com/$id.png",
    screenshotUrlList = listOf("https://example.com/$id-1.png", "https://example.com/$id-2.png"),
    description = "Description $id",
)

fun appSummary(id: String, isFavorite: Boolean = false, rating: Float? = null) = AppSummary(
    id = id,
    name = "App $id",
    description = "Description $id",
    category = "Category $id",
    iconUrl = "https://example.com/$id.png",
    isFavorite = isFavorite,
    rating = rating,
)

fun appDetails(id: String, isFavorite: Boolean = false) = AppDetails(
    id = id,
    name = "App $id",
    description = "Description $id",
    category = "Category $id",
    iconUrl = "https://example.com/$id.png",
    isFavorite = isFavorite,
    developer = "Developer $id",
    ageRating = 12,
    sizeMb = 42.5f,
    screenshotUrls = listOf("https://example.com/$id-1.png", "https://example.com/$id-2.png"),
)
