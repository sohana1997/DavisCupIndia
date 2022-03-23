package com.os.daviscupindia.network.api

import com.os.daviscupindia.ui.home.model.*
import com.os.daviscupindia.ui.home.model.setting.SettingData
import com.os.daviscupindia.utils.handleApiError
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DavisApi {
    @GET("v1/cms/homePage")
    suspend fun getHomePage() : Response<HomeData>

    @GET("v1/setting")
    suspend fun getSetting() : Response<SettingData>

    @POST("v1/auth/register")
    suspend fun userRegister(@Body registerRequest: RegisterRequest) : Response<RegisterResponse>

    @POST("v1/auth/verifyOTP")
    suspend fun verifyOTP(@Body verifyRequest: VerifyRequest) : Response<RegisterResponse>

    @POST("v1/auth/resend")
    suspend fun resendOTP(@Body ressend: ResendOTPRequest) : Response<RegisterResponse>

    @POST("v1/auth/customerLogin")
    suspend  fun customerLogin(@Body customerLoginRequest: CustomerLoginRequest) : Response<RegisterResponse>

    @POST("v1/auth/forgot-password")
    suspend  fun forgotPassword(@Body customerLoginRequest: ForgotPassword) : Response<RegisterResponse>

    @POST("v1/auth/update-password-with-otp")
    suspend  fun updatePasswordWithOtp(@Body customerLoginRequest: CustomerLoginRequest) : Response<RegisterResponse>

}

suspend inline fun <T> safeApiCall(responseFunction: suspend () -> T): T?{
    return try{
        responseFunction.invoke()//Or responseFunction()
    }catch (e: Exception){
        e.printStackTrace()
        handleApiError("")
        null
    }
}