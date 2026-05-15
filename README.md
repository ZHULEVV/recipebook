# Сборник рецептов

Мобильное Android-приложение для хранения и просмотра кулинарных рецептов.

Выполнил: Жулёв Егор Андреевич, группа ИКБО-63-23, РТУ МИРЭА.

---

## Функциональность

- Регистрация и вход через Firebase Authentication
- Восстановление пароля по email
- Лента рецептов с кэшированием через Room
- Поиск рецептов по названию и тегам, история поиска
- Карточка рецепта: ингредиенты, шаги, масштабирование порций, рейтинг
- Избранные рецепты
- Комментарии к рецептам
- План питания с группировкой по типу приёма пищи (завтрак / обед / ужин / перекус)
- Список покупок, сформированный из ингредиентов избранных рецептов
- Переключение светлой / тёмной темы с сохранением в DataStore
- Профиль пользователя

---

## Технологический стек

**UI:** Jetpack Compose, Material 3, Navigation Compose, Coil

**Архитектура:** Clean Architecture (data / domain / presentation), MVVM, StateFlow

**DI:** Hilt (Dagger 2)

**Сеть:** Retrofit 2, OkHttp 3, kotlinx.serialization

**Авторизация:** Firebase Authentication — токен прикрепляется к каждому запросу через `AuthInterceptor`

**Локальное хранилище:** Room 2 (кэш рецептов, избранное, история поиска), DataStore Preferences (настройки темы)

**Тестирование:** JUnit 4, MockK, Turbine, Compose UI Test, Hilt Testing

---

## Структура проекта

```
app/src/main/kotlin/com/recipebook/android/
├── data/
│   ├── auth/          # FirebaseAuthRepositoryImpl
│   ├── local/         # Room entities, DAOs, AppDatabase, DataStore
│   ├── remote/        # Retrofit API, DTOs, маперы, AuthInterceptor
│   ├── repository/    # Реализации репозиториев
│   └── util/          # safeApiCall
├── di/                # Hilt-модули (Firebase, Network, Database, Repository)
├── domain/
│   ├── model/         # Доменные модели
│   ├── repository/    # Интерфейсы репозиториев
│   ├── usecase/       # Use cases
│   └── util/          # Resource<T>
└── presentation/
    ├── auth/          # Login, Register, ForgotPassword
    ├── comments/      # CommentsScreen
    ├── components/    # RecipeCard, LoadingIndicator, ErrorPlaceholder
    ├── favorites/     # FavoritesScreen
    ├── home/          # HomeScreen
    ├── mealplan/      # MealPlanScreen
    ├── navigation/    # Screen, BottomNavBar, AppNavGraph
    ├── profile/       # ProfileScreen, ThemeViewModel
    ├── recipedetails/ # RecipeDetailsScreen
    ├── search/        # SearchScreen
    ├── shoppinglist/  # ShoppingListScreen
    └── theme/         # Color, Theme, Type
```

---

## Сборка и запуск

1. Клонировать репозиторий
2. Открыть проект в Android Studio Hedgehog или новее
3. Поместить `google-services.json` в папку `app/`
4. Запустить бэкенд локально (по умолчанию `http://10.0.2.2:8080/api/v1/`) или обновить `BASE_URL` в `app/build.gradle.kts`
5. Запустить приложение на эмуляторе или устройстве с Android 7.0+

---

## Запуск тестов

```bash
# Unit-тесты
./gradlew test

# UI-тесты (требуется эмулятор или подключённое устройство)
./gradlew connectedAndroidTest
```
