package com.mobilesystems.feedme.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Recipe(
    val recipeId: Int,
    val recipeName: String,
    val recipeLabel: String,
    val recipeNutrition: String = "-",
    val description: String,
    val rating: Float = 0.0F,
    val difficulty: String = "-",
    val cookingTime: String = "-",
    val portions: String = "-",
    val instruction: String = "-",
    val ingredients: List<Product>,
    val imageUrl: String = ""): Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Recipe

        if (recipeId != other.recipeId) return false
        if (recipeName != other.recipeName) return false
        if (recipeLabel != other.recipeLabel) return false
        if (recipeNutrition != other.recipeNutrition) return false
        if (description != other.description) return false
        if (rating != other.rating) return false
        if (difficulty != other.difficulty) return false
        if (cookingTime != other.cookingTime) return false
        if (portions != other.portions) return false
        if (instruction != other.instruction) return false
        if (ingredients != other.ingredients) return false
        if (imageUrl != other.imageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = recipeId
        result = 31 * result + recipeName.hashCode()
        result = 31 * result + recipeLabel.hashCode()
        result = 31 * result + recipeNutrition.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + rating.hashCode()
        result = 31 * result + difficulty.hashCode()
        result = 31 * result + cookingTime.hashCode()
        result = 31 * result + portions.hashCode()
        result = 31 * result + instruction.hashCode()
        result = 31 * result + ingredients.hashCode()
        result = 31 * result + imageUrl.hashCode()
        return result
    }

    override fun toString(): String {
        return "Recipe(recipeId=$recipeId, recipeName='$recipeName', recipeLabel='$recipeLabel', " +
                "recipeNutrition='$recipeNutrition', description='$description', rating=$rating, " +
                "difficulty='$difficulty', cookingTime='$cookingTime', portions='$portions', " +
                "instruction='$instruction', ingredients=$ingredients, imageUrl='$imageUrl')"
    }


}