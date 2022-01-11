package com.mobilesystems.feedme.data.datasource.placeholder

import com.mobilesystems.feedme.domain.model.*
import java.util.*

object UserPlaceholderContent {

    var USER_ITEM: User

    init {
        // Create placeholder recipe
        val labels : MutableList<FoodType> = arrayListOf()
        labels.add(FoodType.VEGAN)
        labels.add(FoodType.MEDITERRANEAN)
        labels.add(FoodType.ASIAN)

        val settings = Settings(true, true, true, labels)
        USER_ITEM = User(0, "https://cdn.pixabay.com/photo/2016/11/21/12/42/beard-1845166_960_720.jpg",
            "james12","James J.", "Dyson", "james.dyson@gmail.com", "hallo123", settings)
    }
}