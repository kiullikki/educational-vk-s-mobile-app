package ru.itmo.storemini.data

import ru.itmo.storemini.domain.appdetails.AppDetails
import ru.itmo.storemini.domain.catalog.AppSummary

fun AppCatalogItemDto.toDomain(isFavorite: Boolean): AppSummary = AppSummary(
    id = id,
    name = name,
    description = description,
    category = category,
    iconUrl = iconUrl,
    isFavorite = isFavorite,
)

fun AppDetailsDto.toDomain(isFavorite: Boolean): AppDetails = AppDetails(
    id = id,
    name = name,
    description = description,
    category = category,
    iconUrl = iconUrl,
    isFavorite = isFavorite,
    developer = developer,
    ageRating = ageRating,
    sizeMb = size,
    screenshotUrls = screenshotUrlList,
)
