package com.mobilesystems.feedme.ui.common.utils

import com.mobilesystems.feedme.domain.model.Product
import java.util.*

fun containsSubstring(products: List<Product>?, ingredient: Product): Boolean{
    var isContained = false
    if(products != null){
        // split multiple words in string
        val wordArray = splitSentenceRegex(ingredient.productName)
        for(word in wordArray){
            // whilst it is not contained search
            if(!isContained) {
                val filtered = products.filter { p ->
                    p.productName.contains(
                        ingredient.productName,
                        true
                    )
                }
                if (filtered.isNotEmpty()) {
                    isContained = true
                }
            }
        }
    }
    return isContained
}

fun splitSentenceRegex(text: String): List<String>{
    return text.split("\\s+".toRegex()).map { word ->
        word.replace("""^[,\.]|[,\.]$""".toRegex(), "")
    }
}

fun String.capitalizeWords(): String = split(" ").joinToString(" ") {
    it.replaceFirstChar { p ->
        if (p.isLowerCase()) p.titlecase(Locale.getDefault()) else p.toString()
    }
}
