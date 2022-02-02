package com.mobilesystems.feedme.ui.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import java.io.File
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import com.google.gson.Gson
import android.content.SharedPreferences
import android.icu.text.DateFormat
import android.net.ParseException
import android.os.Build
import android.util.Log
import com.mobilesystems.feedme.data.request.*
import com.mobilesystems.feedme.data.response.*
import com.mobilesystems.feedme.domain.model.*
import com.mobilesystems.feedme.domain.model.LoggedInUser
import org.json.JSONObject
import java.io.ByteArrayOutputStream

/**
 * All utility functions that are commonly used.
 */

fun getTimeDiff(dateStr: String): Long {
    var result: Long = 0
    if(dateStr.isNotEmpty()) {
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
        val endDateValue: Date = sdf.parse(dateStr)
        val startDateValue = Calendar.getInstance().timeInMillis
        result = TimeUnit.MILLISECONDS.toDays(endDateValue.time - startDateValue)
    }
    return result
}

fun addDaysToCurrentDate(days: Int): Calendar {
    val currentDate = getCurrentDate()
    currentDate.add(Calendar.DATE, days)
    return currentDate
}

fun getCurrentDate(): Calendar {
    val calendar =  Calendar.getInstance()
    val currentDate = calendar.time
    calendar.time = currentDate
    return calendar
}

fun convertDateFormat(dateStr: String): String {
    // formats a date string into the correct format
    val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val outputFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy")
    var inputDate = Date()
    try {
        inputDate = inputFormat.parse(dateStr)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return outputFormat.format(inputDate) ?: dateStr
}

fun convertTokenToUser(context: Context, jwt: String?): LoggedInUser? {
    var user: LoggedInUser? = null
    if(jwt != null){
        val decoded = decodeJWTToken(jwt)
        Log.d("Decoded token", decoded)
        val jsonObj = JSONObject(decoded)
        user = LoggedInUser(
            userId = jsonObj.get("userId") as Int,
            firstName = jsonObj.get("firstName") as String,
            lastName = jsonObj.get("lastName") as String,
            email = jsonObj.get("email") as String
        )
        saveTokenToSharedPreference(context, jwt)
        saveLoggedInUserToSharedPreference(context, user)
    }
    return user
}

// Save objects to shared preferences
fun saveObjectToSharedPreference(
    context: Context,
    preferenceFileName: String?,
    serializedObjectKey: String?,
    `object` : Any?
) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(preferenceFileName, 0)
    val sharedPreferencesEditor = sharedPreferences.edit()
    val gson = Gson()
    val serializedObject = gson.toJson(`object`)
    sharedPreferencesEditor.putString(serializedObjectKey, serializedObject)
    sharedPreferencesEditor.apply()
    Log.d("SharedPreferences", "Saved $`object` to shared preferences!")
}

// Remove object from shared preferences
fun removeObjectFromSharedPreference(
    context: Context,
    preferenceFileName: String?,
    serializedObjectKey: String?,
    `object` : Any?
) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(preferenceFileName, 0)
    val sharedPreferencesEditor = sharedPreferences.edit()
    sharedPreferencesEditor.remove(serializedObjectKey)
    sharedPreferencesEditor.apply()
    Log.d("SharedPreferences", "Removed $`object` from shared preferences!")
}

// Retrieve object from shared preferences
fun <GenericClass> getSavedObjectFromPreference(
    context: Context,
    preferenceFileName: String?,
    preferenceKey: String?,
    classType: Class<GenericClass>?
): GenericClass? {
    val sharedPreferences = context.getSharedPreferences(preferenceFileName, 0)
    if (sharedPreferences.contains(preferenceKey)) {
        val gson = Gson()
        Log.d("SharedPreferences", "Retrieved object from shared preferences!")
        return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType)
    }
    return null
}

fun decodeJWTToken(jwt: String): String {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return "Requires SDK 26"
    val parts = jwt.split(".")
    return try {
        val charset = charset("UTF-8")
        val header = String(Base64.getUrlDecoder().decode(parts[0].toByteArray(charset)), charset)
        val payload = String(Base64.getUrlDecoder().decode(parts[1].toByteArray(charset)), charset)
        header
        payload
    } catch (e: Exception) {
        "Error parsing JWT: $e"
    }
}

fun saveTokenToSharedPreference(context: Context, jwt: String){
    saveObjectToSharedPreference(context,
        "mPreference",
        "jwtToken", jwt)
}

fun saveLoggedInUserToSharedPreference(context: Context, user: LoggedInUser){
    saveObjectToSharedPreference(context,
        "mPreference",
        "loggedInUser", user)
}

fun removeLoggedInUserFromSharedPreferences(context: Context, user: LoggedInUser){
    removeObjectFromSharedPreference(context,
        "mPreference",
        "loggedInUser", user)
}

fun removeTokenFromSharedPreferences(context: Context, jwt: String){
    removeObjectFromSharedPreference(context,
        "mPreference",
        "jwtToken", jwt)
}

fun removeAllValuesFromSharedPreferences(context: Context, preferenceFileName: String = "mPreference"){
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(preferenceFileName, 0)
    val sharedPreferencesEditor = sharedPreferences.edit()
    sharedPreferencesEditor.clear()
    sharedPreferencesEditor.apply()
    Log.d("SharedPreferences", "All entries deleted!")
}

fun getLoggedInUser(context: Context) : LoggedInUser? {
    //Retrieve current user id stored in preference
    val loggedInUser = getSavedObjectFromPreference(context, "mPreference",
        "loggedInUser", LoggedInUser::class.java)
    Log.d("SharedPreferences", "Retrieved user with id ${loggedInUser?.userId} from shared preferences!")
    return loggedInUser
}

fun getJWTToken(context: Context) : String? {
    //Retrieve current user id stored in preference
    val jwt = getSavedObjectFromPreference(context, "mPreference",
        "jwtToken", String::class.java)
    Log.d("SharedPreferences", "Retrieved jwt token $jwt from shared preferences!")
    return jwt
}

fun convertAllowSettingRequest(userId: Int, allow: Boolean): UserAllowSettingsRequest {
    var allowed = 0
    if (allow) {
        allowed = 1
    }
    return UserAllowSettingsRequest(userId, allowed)
}

fun convertUserDietryTagRequest(user: User): UserDietryTagRequest?{
    val userId = user.userId
    val foodTypeList = user.dietaryPreferences
    var dietryTag: ProductTagRequest? = null
    var userDietryTagRequest: UserDietryTagRequest? = null

    try {
        if (foodTypeList != null) {
            for (label in foodTypeList) {
                dietryTag = ProductTagRequest(0, label.toString())
            }
        } else {
            dietryTag = ProductTagRequest(0, "")
        }
        userDietryTagRequest = UserDietryTagRequest(userId, dietryTag)
    }catch (e: Exception){
        e.stackTrace
    }
    return userDietryTagRequest
}

fun convertUpdateImageRequest(userId: Int, image: Image?): ImageRequest? {
    Log.d("Utils", "Convert image.")
    var userImageRequest: ImageRequest? = null
    var imageByteStr: String? = null
    try {
        if(image != null){
            if(image.bitmap != null){
                val imageBytes = convertBitmapToByteArray(image.bitmap)
                imageByteStr = bytesToBase64(imageBytes) // base64 encoded string
                Log.d("Utils", "Base64 encoded the image string.")
            }
            userImageRequest = ImageRequest(image.imageId, image.imageName, image.imageUrl, imageByteStr)
        }
    }catch (e: Exception){
        e.stackTrace
    }
    return userImageRequest
}

fun convertUpdateUserRequest(user: User): UpdateUserRequest {
    val userId = user.userId
    val email = user.email
    val firstName = user.firstName
    val lastName = user.lastName
    return UpdateUserRequest(firstName, lastName, email, userId)
}

fun convertUserResponse(userResponse: UserResponse?) : User? {
    var user: User? = null
    try {
        if (userResponse != null) {
            Log.d("Utils", "Convert $userResponse object to User.")
            val dietaryPreferences: MutableList<FoodType> = mutableListOf()
            if (userResponse.dietaryPreferences != null) {
                for (tag in userResponse.dietaryPreferences) {
                    val dietPref = FoodType.from(tag.label)
                    if (dietPref != null) {
                        dietaryPreferences.add(dietPref)
                        Log.d("Utils", "User convert dietry prefs to $dietPref")
                    }
                }
            } else {
                Log.e("Utils", "User dietary preferences are null.")
            }
            var settings: Settings? = null
            if (userResponse.userSettings != null) {
                val reminder = userResponse.userSettings.remindBeforeProductExpiration
                val suggestion = userResponse.userSettings.suggestProductsForShoppingList
                val pushNotification = userResponse.userSettings.allowPushNotifications
                settings = Settings(reminder, pushNotification, suggestion)
                Log.e("Utils", "Converted user setting $settings.")
            }

            var userImage: Image? = null
            if (userResponse.userImage != null) {
                var nbitmap: Bitmap? = null
                if(!userResponse.userImage.base64ImageString.isNullOrEmpty()){
                    val byteArray = base64ToBytes(userResponse.userImage.base64ImageString)
                    if(byteArray != null) {
                        nbitmap = convertByteArrayToBitmap(byteArray)
                        Log.e("Utils", "Converted to bitmap $nbitmap.")
                    }else{
                        Log.e("Utils", "User image byte array is empty!")
                    }
                }
                userImage = Image(
                    imageId = userResponse.userImage.imageId,
                    imageName = userResponse.userImage.imageName,
                    imageUrl = userResponse.userImage.imageUrl,
                    bitmap = nbitmap
                )
            }else{
                Log.e("Utils", "User image is empty.")
            }

            user = User(
                userId = userResponse.userId,
                firstName = userResponse.firstName,
                lastName = userResponse.lastName,
                email = userResponse.email,
                password = userResponse.password,
                userSettings = settings,
                dietaryPreferences = dietaryPreferences,
                userImage = userImage
            )
            Log.d("Utils", "Konvertierter User $user")
        } else {
            Log.d("Utils", "UserResponse is null!")
        }
    }catch(e: Exception){
        e.stackTrace
        Log.d("Utils", "Exception $e")
    }
    return user
}
fun convertExpiringProductList(expProductListResponse: ExpiringProductResponse?) : List<Product>{
    val products: MutableList<Product> = mutableListOf()
    try {
        if(expProductListResponse!= null && expProductListResponse.isNotEmpty()) {
            for(product in expProductListResponse) {
                var image: Image? = null
                val productTags: MutableList<Label> = mutableListOf()
                if (product.productImage != null) {
                    // only convert image if available
                    var nbitmap: Bitmap? = null
                    if(!product.productImage.base64ImageString.isNullOrEmpty()){
                        val byteArray = base64ToBytes(product.productImage.base64ImageString)
                        if(byteArray != null) {
                            nbitmap = convertByteArrayToBitmap(byteArray)
                        }
                    }
                    image = Image(
                        imageId = product.productImage.imageId,
                        imageName = product.productImage.imageName,
                        imageUrl = product.productImage.imageUrl,
                        bitmap = nbitmap
                    )
                }
                if (product.productTags != null) {
                    for (tag in product.productTags) {
                        val nlabel = Label.from(tag.label)
                        if (nlabel != null) {
                            productTags.add(nlabel)
                        }else{
                            Log.d("Utils", "Label is null.")
                        }
                    }
                    val nProduct = Product(
                        productId = product.productId,
                        productName = product.productName,
                        expirationDate = convertDateFormat(product.expirationDate),
                        labels = productTags,
                        quantity = product.quantity,
                        manufacturer = product.manufacturer,
                        nutritionValue = product.nutritionValue,
                        productImage = image
                    )
                    products.add(nProduct)
                }
            }
        }
    } catch (e: Exception) {
        e.stackTrace
        Log.e("Convert expiring products.", "Error $e")
    }
    return products
}

fun convertRecipeResponse(recipeListResponse: RecipeListResponse?) : List<Recipe> {
    val recipes: MutableList<Recipe> = mutableListOf()

    try {
        if(recipeListResponse != null && recipeListResponse.isNotEmpty()) {
            Log.d("Utils", "Convert ${recipeListResponse.size} objects to Recipelist.")
            for(recipe in recipeListResponse) {

                Log.d("Utils", "Convert $recipe object to Recipe.")
                val ingredients = recipe.ingredients
                val nIngredients: MutableList<Product> = mutableListOf()
                if (ingredients != null && ingredients.isNotEmpty()) {
                    // TODO: Fix no nested for-loops
                    Log.d("Utils", "Convert $ingredients object to Ingredient.")
                    for (ingredient in ingredients) {
                        val nIngredient = Product(
                            productId = ingredient.ingredientId,
                            productName = ingredient.ingredientName,
                            expirationDate = "",
                            labels = null,
                            quantity = ingredient.quantity,
                            manufacturer = "",
                            nutritionValue = "",
                            productImage = null
                        )
                        nIngredients.add(nIngredient)
                    }
                }

                val nRecipe = Recipe(
                    recipeId = recipe.recipeId,
                    recipeName = recipe.recipeName,
                    recipeNutrition = recipe.recipeNutrition,
                    description = recipe.description,
                    cummulativeRating = recipe.cummulativeRating,
                    amountOfRatings = recipe.amountOfRatings,
                    difficulty = recipe.difficulty,
                    cookingTime = recipe.cookingTime,
                    instructions = recipe.instructions,
                    imageUrl = recipe.imageUrl,
                    ingredients = nIngredients.toList()
                )
                recipes.add(nRecipe)
            }
        }
    } catch (e: Exception) {
        e.stackTrace
        Log.e("Convert Recipe", "Error $e")
    }
    return recipes
}

fun convertBarcodeResultToProduct(product: BarcodeProductResponse?): Product? {
    var nProduct: Product? = null
    try{
        // create new image th
        val productImage = Image(
            imageId = 0,
            imageName = "Produktbild",
            imageUrl = "",
            bitmap = null
        )
        // create product
        val labels: MutableList<Label> = arrayListOf()
        nProduct = Product(
            productId = 0,
            productName = product?.label ?: "Bitte Namen eingeben.",
            expirationDate = "Bitte Datum eingeben.",
            labels = labels,
            quantity = "1 Stück",
            manufacturer = product?.brand ?: "Bitte Namen eingeben.",
            nutritionValue = product?.nutrients ?: " kcal",
            productImage = productImage
        )
    } catch (e: Exception) {
        e.stackTrace
        Log.e("Convert Barcode", "Error $e")
    }
    return nProduct
}

fun convertInventoryListResponse(inventoryListResponse: InventoryListResponse?) : List<Product>{
    val products: MutableList<Product> = mutableListOf()
    try{
        if(inventoryListResponse != null) {
            Log.d("Utils", "Convert $inventoryListResponse object to Productlist.")
            if (inventoryListResponse.inventoryList.isNotEmpty()) {
                for (product in inventoryListResponse.inventoryList) {
                    Log.d("Utils", "Convert ${product.productId} object to Product.")
                    val nImage = createProductImg(product)
                    val productTags = createProductTags(product)
                    val nProduct = Product(
                        productId = product.productId,
                        productName = product.productName,
                        expirationDate = convertDateFormat(product.expirationDate),
                        labels = productTags,
                        quantity = product.quantity,
                        manufacturer = product.manufacturer,
                        nutritionValue = product.nutritionValue,
                        productImage = nImage
                    )
                    products.add(nProduct)
                }
            }
        }
    } catch (e: Exception) {
        e.stackTrace
        Log.e("Convert Inventorylist", "Error $e")
    }
    return products
}

private fun createProductTags(product: ProductResponse): MutableList<Label> {
    val productLabels = mutableListOf<Label>()
    if (product.productTags != null) {
        for (tag in product.productTags) {
            val nlabel = Label.from(tag.label)
            if (nlabel != null) {
                productLabels.add(nlabel)
            }else{
                Log.d("Utils", "Label is null.")
            }
        }
    }
    return productLabels
}

private fun createProductImg(product: ProductResponse): Image?{
    var nImage: Image? = null
    if (product.productImage != null) {
        // only convert image if available
        var nBitmap: Bitmap? = null
        if(!product.productImage.base64ImageString.isNullOrEmpty()){
            val byteArray = base64ToBytes(product.productImage.base64ImageString)
            if(byteArray != null) {
                nBitmap = convertByteArrayToBitmap(byteArray)
            }
        } else{
            Log.d("Utils", "No bitmap found.")
        }
        nImage = Image(
            imageId = product.productImage.imageId,
            imageName = product.productImage.imageName,
            imageUrl = product.productImage.imageUrl,
            bitmap = nBitmap
        )
    }
    return nImage
}

fun convertShoppingListResponse(shoppingListResponse: ShoppingListResponse?) : List<Product> {
    val products: MutableList<Product> = mutableListOf()
    try{
        if (shoppingListResponse != null) {
            Log.d("Utils", "Convert ShoppingList $shoppingListResponse object to Shoppinglist.")
            if (shoppingListResponse.shoppingList.isNotEmpty()) {
                for (product in shoppingListResponse.shoppingList) {
                    Log.d("Utils", "Convert $product object to Product.")
                    val newProduct = Product(
                        productId = product.productId,
                        productName = product.productName,
                        expirationDate = convertDateFormat(product.expirationDate),
                        labels = null,
                        quantity = product.quantity,
                        manufacturer = product.manufacturer,
                        nutritionValue = product.nutritionValue,
                        productImage = null
                    )
                    products.add(newProduct)
                }
            }
        }
    } catch (e: Exception) {
        e.stackTrace
        Log.e("Convert Shoppinglist", "Error $e")
    }
    Log.d("Utils","Konvertierte Shoppingliste: $products")
    return products
}

fun convertProductRequest(userId: Int, product: Product): ProductRequest?{
    var productRequest: ProductRequest? = null
    val productTags: MutableList<ProductTagRequest> = mutableListOf()
    val productImage: ImageRequest?
    var newTag: ProductTagRequest?

    try {
        //product
        val productId = product.productId
        val productName = product.productName
        val quantity = product.quantity
        var expirationDate = product.expirationDate
        val nutritionValue = product.nutritionValue
        val manufacturer = product.manufacturer
        val image = product.productImage
        val labels: MutableList<Label>? = product.labels

        if(expirationDate.isEmpty()){
            expirationDate = "2022-02-02"
        }

        if(image != null){
            val newImageId = image.imageId
            val newImageName = image.imageName
            val newImageUrl = image.imageUrl
            val newImageBitmap = image.bitmap
            var newImageBase64Str: String? = null
            if(newImageBitmap != null){
                val newImageByteArray: ByteArray? = convertBitmapToByteArray(newImageBitmap)
                newImageBase64Str = bytesToBase64(newImageByteArray)
            }
            productImage = ImageRequest(newImageId, newImageName, newImageUrl, newImageBase64Str)
        }else{
            productImage = ImageRequest(productId, "Produktbild", "", null)
        }

        if(labels != null) {
            for(label in labels){
                newTag = ProductTagRequest(0, label.toString())
                productTags.add(newTag)
            }
        }else{
            newTag = ProductTagRequest(0, "")
            productTags.add(newTag)
        }

        productRequest = ProductRequest(
            userId,
            productId,
            productName,
            expirationDate,
            quantity,
            manufacturer,
            nutritionValue,
            productImage,
            productTags
        )
        Log.d("Utils", "Converted added product to $productRequest")
    } catch(e: Exception){
        e.stackTrace
        Log.e("Convert Shoppinglist Product", "Error $e")
    }
    return productRequest
}

fun convertProductWithNewProductId(productIdResult: ProductIdResponse, product: Product): Product {
    return Product(
        productIdResult.productId,
        product.productName,
        product.expirationDate,
        product.labels,
        product.quantity,
        product.manufacturer,
        product.nutritionValue,
        product.productImage)
}

fun convertShoppingListProductIDRequest(userId: Int, product: Product): ShoppingListProductIDRequest {
    return ShoppingListProductIDRequest(userId, product.productId)
}

fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
    var width = bitmap.width
    var height = bitmap.height

    val bitmapRatio = width/height
    if(bitmapRatio > 1){
        width = maxSize
        height = width/bitmapRatio
    } else{
        height = maxSize
        width = height * bitmapRatio
    }
    return Bitmap.createScaledBitmap(bitmap, width, height, true)
}

fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray? {
    var byteArray: ByteArray? = null
    try {
        val stream = ByteArrayOutputStream()
        val compressFormat = Bitmap.CompressFormat.JPEG
        Log.d("Utils", "Convert with compress format $compressFormat.")
        bitmap.compress(compressFormat, 100, stream)
        byteArray = stream.toByteArray()
        Log.d("Utils", "Convert bitmap to byte array.")
        // bitmap.recycle()
    } catch (e: java.lang.Exception) {
        e.stackTrace
        Log.d("Utils", "Error: Convert bitmap to byte array.")
    }
    return byteArray
}

fun convertByteArrayToBitmap(bytes: ByteArray): Bitmap?{
    var bitmap: Bitmap? = null
    try {
        bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.size)
        Log.d("Utils", "Convert byte array to bitmap.")
    } catch (e: java.lang.Exception) {
        e.stackTrace
        Log.d("Utils", "Error: Convert byte array to bitmap.")
    }
    return bitmap
}

fun bytesToBase64(bytes: ByteArray?): String? {
    var result: String? = null
    try {
        result = Base64.getEncoder().encodeToString(bytes)
        Log.d("Utils", "Convert byte array to base64 string.")
    }catch (e: Exception){
        e.printStackTrace()
        Log.d("Utils", "Error: Convert byte array to base64 string.")
    }
    return result
}

fun base64ToBytes(base64Str: String?): ByteArray? {
    var result: ByteArray? = null
    try{
        result = Base64.getDecoder().decode(base64Str)
        Log.d("Utils", "Convert base64 string to bytes.")
    }catch (e: Exception){
        e.printStackTrace()
        Log.d("Utils", "Error: Convert base64 string to bytes.")
    }
    return result
}

fun filePathToBitmap(pathname: String): Bitmap {
    val imgFile = File(pathname)
    return BitmapFactory.decodeFile(imgFile.absolutePath)
}

fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) {
        if (drawable.bitmap != null) {
            return drawable.bitmap
        }
    }
    val bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
        Bitmap.createBitmap(
            1,
            1,
            Bitmap.Config.ARGB_8888
        ) // Single color bitmap will be created of 1x1 pixel
    } else {
        Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
    }
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

//Returns `true` if this string is empty or consists solely of whitespace characters.
fun CharSequence.isBlank(): Boolean = length == 0 || indices.all { this[it].isWhitespace() }



