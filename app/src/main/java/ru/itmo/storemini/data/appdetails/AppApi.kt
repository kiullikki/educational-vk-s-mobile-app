package ru.itmo.storemini.data.appdetails

import kotlinx.coroutines.delay
import ru.itmo.storemini.domain.appdetails.AppApi
import ru.itmo.storemini.domain.appdetails.AppItem

class FakeAppApi : AppApi {
    override suspend fun getApps(): List<AppItem> {
        delay(300)
        return mockAllApps
    }
}
