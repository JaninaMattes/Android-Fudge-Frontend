package com.mobilesystems.feedme.data.remote

import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.domain.model.Product
import com.mobilesystems.feedme.domain.model.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 *  Retrofit Http Client
 *
 *  @Path	Variable substitution for the API endpoint (i.e. username will be swapped for {username} in the URL endpoint).
 *  @Query	Specifies the query key name with the value of the annotated parameter.
 *  @Body	Payload for the POST call (serialized from a Java object to a JSON string)
 *  @Header	Specifies the header with the value of the annotated parameter
 *
 *  Retrofit Tutorial: https://square.github.io/retrofit/
 */
interface FoodTrackerApi {

    @FormUrlEncoded
    @POST("/api/login")
    suspend fun loginUser(@Field("email") email: String, @Field("password") password: String): Response<User>

    @FormUrlEncoded
    @POST("/api/register")
    suspend fun registerUser(@Field("firstName") username: String, @Field("email")email: String,
                             @Field("password")password: String, @Field("passwordConfirm")passwordConfirm: String): Response<User>

    @FormUrlEncoded
    @POST("/api/logout")
    suspend fun logout(@Field("email") email: String, @Field("password") password: String)

    suspend fun getAllProductsInInventoryList(userId: Int): Response<List<Product>>
}