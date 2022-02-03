package com.mobilesystems.feedme.domain.model

/**
 * The Label class contains the expiration date of a product and its type.
 * Tutorial: https://www.baeldung.com/kotlin/enum
 *           https://kotlinlang.org/docs/enum-classes.html
 */
enum class Label(val label: String): IConsumptionLimit {

    WHITE_MEAT("Helles Fleisch"){
        override fun calculateExpirationDays() = 3
    },
    RED_MEAT("Rotes Fleisch"){
        override fun calculateExpirationDays() = 5
    },
    SAUSAGES("Wurst"){
        override fun calculateExpirationDays() = 5
    },
    FISH("Fisch"){
        override fun calculateExpirationDays() = 5
    },
    EGGS("Eier"){
        override fun calculateExpirationDays() = 7
    },
    DAIRY("Milchprodukt"){
        override fun calculateExpirationDays() = 7
    },
    VEGETABLE("Gemüse"){
        override fun calculateExpirationDays() = 10
    },
    FRUIT("Obst"){
        override fun calculateExpirationDays() = 14
    },
    NUTS("Nüsse"){
        override fun calculateExpirationDays() = 30
    },
    MUESLI("Müsli"){
        override fun calculateExpirationDays() = 30
    },
    DRY_PRODUCT("Trockenprodukt"){
        override fun calculateExpirationDays() = 30
    },
    FROZEN_MEAL("Tiefkühlprodukt"){
        override fun calculateExpirationDays() = 14
    },
    DRINK("Getränk"){
        override fun calculateExpirationDays() = 30
    },
    CEREAL_PRODUCT("Getreideprodukt"){
        override fun calculateExpirationDays() = 30
    },
    OIL("Öl"){
        override fun calculateExpirationDays() = 30
    },
    BUTTER("Butter"){
        override fun calculateExpirationDays() = 30
    },
    BAKERY("Backwaren"){
        override fun calculateExpirationDays() = 4
    },
    NIBBLES("Knabberein"){
        override fun calculateExpirationDays() = 14
    },
    CONFECTIONARY("Süßigkeiten"){
        override fun calculateExpirationDays() = 14
    },
    HONEY("Honig"){
        override fun calculateExpirationDays() = 30
    },
    SPICES("Gewürze"){
        override fun calculateExpirationDays() = 90
    },
    SALT("Salz"){
        override fun calculateExpirationDays() = 365
    },
    SUGGAR("Zucker"){
        override fun calculateExpirationDays() = 365
    };

    // custom properties
    var printableLabel : String? = null

    // custom method
    fun customToString(): String {
        return "[${label}] -> $printableLabel"
    }

    companion object {
        fun from(label: String): Label? = values().find { it.label == label }
    }
}

interface IConsumptionLimit {
    fun calculateExpirationDays(): Int
}