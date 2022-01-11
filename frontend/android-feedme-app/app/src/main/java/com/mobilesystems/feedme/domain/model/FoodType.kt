package com.mobilesystems.feedme.domain.model

enum class FoodType(val label: String) {

    VEGAN("Vegan"),
    VEGETARIAN("Vegetarisch"),
    LOW_CARB("Low Carb"),
    FAST_FOOD("Fast Food"),
    MEDITERRANEAN("Mediterrane Küche"),
    AMERICAN("Ame"),
    ASIAN("Asiatisch"),
    PALEO("Paleo");

    // custom properties
    var printableLabel : String? = null

    // custom method
    fun customToString(): String {
        return "[${label}] -> $printableLabel"
    }

    companion object {
        fun from(label: String): Label? = Label.values().find { it.label == label }
    }
}