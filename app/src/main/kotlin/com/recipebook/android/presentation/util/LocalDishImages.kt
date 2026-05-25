package com.recipebook.android.presentation.util

import com.recipebook.android.R

object LocalDishImages {

    private val byTitle: Map<String, Int> = mapOf(
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

    private val byName: Map<String, Int> = mapOf(
        "dish_borscht"      to R.drawable.dish_borscht,
        "dish_kotlety"      to R.drawable.dish_kotlety,
        "dish_bliny"        to R.drawable.dish_bliny,
        "dish_pelmeni"      to R.drawable.dish_pelmeni,
        "dish_olivier"      to R.drawable.dish_olivier,
        "dish_carbonara"    to R.drawable.dish_carbonara,
        "dish_greek_salad"  to R.drawable.dish_greek_salad,
        "dish_teriyaki"     to R.drawable.dish_teriyaki,
        "dish_buckwheat"    to R.drawable.dish_buckwheat,
        "dish_shashlik"     to R.drawable.dish_shashlik,
        "dish_risotto"      to R.drawable.dish_risotto,
        "dish_syrniki"      to R.drawable.dish_syrniki,
        "dish_pizza"        to R.drawable.dish_pizza,
        "dish_salmon"       to R.drawable.dish_salmon,
        "dish_okroshka"     to R.drawable.dish_okroshka,
        "dish_tomyam"       to R.drawable.dish_tomyam,
        "dish_lasagna"      to R.drawable.dish_lasagna,
        "dish_plov"         to R.drawable.dish_plov,
        "dish_omlet"        to R.drawable.dish_omlet,
        "dish_minestrone"   to R.drawable.dish_minestrone,
        "dish_guacamole"    to R.drawable.dish_guacamole,
        "dish_ramen"        to R.drawable.dish_ramen,
        "dish_tiramisu"     to R.drawable.dish_tiramisu,
        "dish_napoleon"     to R.drawable.dish_napoleon,
        "dish_chicken_soup" to R.drawable.dish_chicken_soup,
        "dish_fried_rice"   to R.drawable.dish_fried_rice
    )

    fun forTitle(title: String): Int? = byTitle[title]

    fun forUrl(imageUrl: String?): Int? {
        if (imageUrl == null || !imageUrl.startsWith("local://")) return null
        return byName[imageUrl.removePrefix("local://")]
    }

    fun resolveModel(imageUrl: String?, title: String): Any? =
        forUrl(imageUrl) ?: imageUrl
}
