package com.mobilesystems.feedme.data.remote

import com.mobilesystems.feedme.data.request.LoginRequest
import com.mobilesystems.feedme.data.request.LogoutRequest
import com.mobilesystems.feedme.data.request.RegisterRequest
import com.mobilesystems.feedme.data.response.UserResponse
import com.mobilesystems.feedme.domain.model.User
import retrofit2.Response
import retrofit2.http.*

/**
 *  FoodTrackerApi interface makes use of the Retrofit Http Client library, with: *
 *  @Path	Variable substitution for the API endpoint (i.e. username will be swapped for {username} in the URL endpoint).
 *  @Query	Specifies the query key name with the value of the annotated parameter.
 *  @Body	Payload for the POST call (serialized from a Java object to a JSON string)
 *  @Header	Specifies the header with the value of the annotated parameter
 *
 *  Retrofit Tutorial: https://square.github.io/retrofit/
 *                     https://github.com/square/retrofit/issues/3626
 *                     https://johncodeos.com/how-to-make-post-get-put-and-delete-requests-with-retrofit-using-kotlin/
 */
interface FoodTrackerApi {

    /**
     * All requests to user service
     */
    @POST("/api/users/login")
    suspend fun loginUser(@Body request: LoginRequest) : Response<Map<String, String>>

    @POST("/api/users/register")
    suspend fun registerUser(@Body request: RegisterRequest) : Response<Map<String, String>>

    @POST("/api/users/logout")
    suspend fun logout(@Body request: LogoutRequest) : Response<Int>

    @GET("/api/users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int) : Response<UserResponse>

    @PUT("/api/users/update")
    suspend fun updateUser(@Query("user") user: User) : Response<Int>

    @DELETE("/api/users/delete")
    suspend fun deleteUser(@Query("userId") userId: Int) : Response<Int>

    @GET("/api/users/loggedin")
    suspend fun getIsUserLoggedIn(@Query("userId") userId: Int) : Response<Boolean>

    // suspend fun getAllProductsInInventoryList(userId: Int): Response<List<Product>>
}