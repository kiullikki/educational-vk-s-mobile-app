package ru.itmo.storemini.data

import ru.itmo.storemini.domain.appdetails.AppItem

fun AppItemDto.toDomain(): AppItem = AppItem(
    id = id,
    name = name,
    description = description,
    fullDescription = fullDescription,
    category = category,
    isFavorite = isFavorite,
    developer = developer,
    ageRating = ageRating,
    sizeMb = sizeMb,
    iconRes = iconRes,
)
 