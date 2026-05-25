package com.recipebook.android.presentation.util

import com.recipebook.android.R

object LocalDishImages {

    private val map: Map<String, Int> = mapOf(
        "Борщ"                        to R.drawable.dish_borscht,
        "Котлеты по-домашнему"        to R.drawable.dish_kotlety,
        "Блины"                       to R.drawable.dish_bliny,
        "Пельмени"                    to R.drawable.dish_pelmeni,
        "Салат Оливье"                to R.drawable.dish_olivier,
        "Паста Карбонара"             to R.drawable.dish_carbonara,
        "Греческий салат"             to R.drawable.dish_greek_salad,
        "Курица терияки"              to R.drawable.dish_teriyaki,
        "Гречневая каша с грибами"    to R.drawable.dish_buckwheat,
        "Шашлык из курицы"            to R.drawable.dish_shashlik,
        "Ризотто с грибами"           to R.drawable.dish_risotto,
        "Сырники"                     to R.drawable.dish_syrniki,
        "Пицца Маргарита"             to R.drawable.dish_pizza,
        "Запечённый лосось"           to R.drawable.dish_salmon,
        "Окрошка"                     to R.drawable.dish_okroshka,
        "Том-ям"                      to R.drawable.dish_tomyam,
        "Лазанья болоньезе"           to R.drawable.dish_lasagna,
        "Плов узбекский"              to R.drawable.dish_plov,
        "Омлет"                       to R.drawable.dish_omlet,
        "Суп минестроне"              to R.drawable.dish_minestrone,
        "Гуакамоле"                   to R.drawable.dish_guacamole,
        "Рамен с курицей"             to R.drawable.dish_ramen,
        "Тирамису"                    to R.drawable.dish_tiramisu,
        "Торт Наполеон"               to R.drawable.dish_napoleon,
        "Куриный суп"                 to R.drawable.dish_chicken_soup,
        "Жареный рис по-азиатски"     to R.drawable.dish_fried_rice
    )

    fun forTitle(title: String): Int? = map[title]
}
