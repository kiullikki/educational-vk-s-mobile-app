package com.example.myapplication.data

import androidx.annotation.DrawableRes
import com.example.myapplication.R

data class AppItem(
    @DrawableRes val iconRes: Int,
    val name: String,
    val description: String,
    val category: String,
    val id: Int,
    val isFavorite: Boolean,
)

val mockAllApps: List<AppItem> = listOf(
    AppItem(R.drawable.ic_tech_kotlin, "Kotlin", "Основной язык Android-разработки", "Языки", id = 1, isFavorite = true),
    AppItem(R.drawable.ic_tech_compose, "Jetpack Compose", "Декларативный UI-фреймворк", "UI", id = 2, isFavorite = true),
    AppItem(R.drawable.ic_tech_android, "Android", "Мобильная платформа от Google", "Платформы", id = 3, isFavorite = true),
    AppItem(R.drawable.ic_tech_studio, "Android Studio", "IDE для разработки под Android", "Инструменты", id = 4, isFavorite = false),
    AppItem(R.drawable.ic_tech_gradle, "Gradle", "Система сборки проектов", "Инструменты", id = 5, isFavorite = false),
    AppItem(R.drawable.ic_tech_git, "Git", "Система контроля версий", "Инструменты", id = 6, isFavorite = true),
    AppItem(R.drawable.ic_tech_github, "GitHub", "Хостинг репозиториев", "Инструменты", id = 7, isFavorite = false),
    AppItem(R.drawable.ic_tech_firebase, "Firebase", "Backend-сервисы: БД, аналитика, пуши", "Backend", id = 8, isFavorite = false),
    AppItem(R.drawable.ic_tech_java, "Java", "Первый язык платформы Android", "Языки", id = 9, isFavorite = false),
    AppItem(R.drawable.ic_tech_figma, "Figma", "Дизайн интерфейсов и прототипы", "Дизайн", id = 10, isFavorite = true),
    AppItem(R.drawable.ic_tech_material, "Material Design", "Дизайн-система от Google", "Дизайн", id = 11, isFavorite = false),
    AppItem(R.drawable.ic_tech_sqlite, "SQLite", "Встроенная база данных", "Данные", id = 12, isFavorite = false),
    AppItem(R.drawable.ic_tech_junit, "JUnit", "Фреймворк юнит-тестов", "Тестирование", id = 13, isFavorite = true),
    AppItem(R.drawable.ic_tech_postman, "Postman", "Проверка REST API", "Инструменты", id = 14, isFavorite = false),
    AppItem(R.drawable.ic_tech_flutter, "Flutter", "Кроссплатформенный UI-фреймворк", "UI", id = 15, isFavorite = false),
    AppItem(R.drawable.ic_tech_dart, "Dart", "Язык Flutter", "Языки", id = 16, isFavorite = false),
    AppItem(R.drawable.ic_tech_swift, "Swift", "Язык iOS-разработки", "Языки", id = 17, isFavorite = true),
    AppItem(R.drawable.ic_tech_typescript, "TypeScript", "Типизированный JavaScript", "Языки", id = 18, isFavorite = false),
    AppItem(R.drawable.ic_tech_python, "Python", "Скрипты, бэкенд и ML", "Языки", id = 19, isFavorite = true),
    AppItem(R.drawable.ic_tech_docker, "Docker", "Контейнеризация приложений", "DevOps", id = 20, isFavorite = false),
    AppItem(R.drawable.ic_tech_postgres, "PostgreSQL", "Реляционная база данных", "Данные", id = 21, isFavorite = false),
    AppItem(R.drawable.ic_tech_mongodb, "MongoDB", "NoSQL база данных", "Данные", id = 22, isFavorite = false),
    AppItem(R.drawable.ic_tech_graphql, "GraphQL", "Язык запросов к API", "Backend", id = 23, isFavorite = true),
    AppItem(R.drawable.ic_tech_spring, "Spring", "Фреймворк для бэкенда на JVM", "Backend", id = 24, isFavorite = false),
    AppItem(R.drawable.ic_tech_idea, "IntelliJ IDEA", "IDE от JetBrains", "Инструменты", id = 25, isFavorite = false),
    AppItem(R.drawable.ic_tech_linux, "Linux", "Ядро, на котором живёт Android", "Платформы", id = 26, isFavorite = true),
)

val mockFavoriteApps: List<AppItem> = mockAllApps.filter { it.isFavorite }
