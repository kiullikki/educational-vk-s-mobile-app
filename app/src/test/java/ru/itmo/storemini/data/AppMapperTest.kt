package ru.itmo.storemini.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ru.itmo.storemini.appDetails
import ru.itmo.storemini.appDetailsDto
import ru.itmo.storemini.appSummary
import ru.itmo.storemini.catalogItemDto

class AppMapperTest {

    @Test
    fun `catalog dto maps to favorite domain summary test`() {
        val dto = catalogItemDto(id = "app-1", name = "Store App")

        val result = dto.toDomain(isFavorite = true)

        assertEquals("app-1", result.id)
        assertEquals("Store App", result.name)
        assertEquals("Description app-1", result.description)
        assertEquals("Category app-1", result.category)
        assertEquals("https://example.com/app-1.png", result.iconUrl)
        assertEquals(true, result.isFavorite)
    }

    @Test
    fun `catalog dto maps to non favorite summary without rating test`() {
        val result = catalogItemDto(id = "app-1").toDomain(isFavorite = false)

        assertEquals(false, result.isFavorite)
        assertNull(result.rating)
    }

    @Test
    fun `details dto maps all fields to domain model test`() {
        val dto = appDetailsDto(id = "app-2", name = "Details App")

        val result = dto.toDomain(isFavorite = true)

        assertEquals("app-2", result.id)
        assertEquals("Details App", result.name)
        assertEquals("Developer app-2", result.developer)
        assertEquals("Category app-2", result.category)
        assertEquals(12, result.ageRating)
        assertEquals(42.5f, result.sizeMb)
        assertEquals("https://example.com/app-2.png", result.iconUrl)
        assertEquals(dto.screenshotUrlList, result.screenshotUrls)
        assertEquals("Description app-2", result.description)
        assertEquals(true, result.isFavorite)
    }

    @Test
    fun `domain summary maps back to catalog dto without ui only fields test`() {
        val domain = appSummary(id = "app-3", isFavorite = true, rating = 4.8f)

        val result = domain.toDto()

        assertEquals(
            AppCatalogItemDto(
                id = "app-3",
                name = "App app-3",
                description = "Description app-3",
                category = "Category app-3",
                iconUrl = "https://example.com/app-3.png",
            ),
            result,
        )
    }

    @Test
    fun `domain details maps back to api dto test`() {
        val domain = appDetails(id = "app-4", isFavorite = true)

        val result = domain.toDto()

        assertEquals(appDetailsDto(id = "app-4"), result)
    }
}
