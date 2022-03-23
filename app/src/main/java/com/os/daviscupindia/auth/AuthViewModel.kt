package com.os.daviscupindia.auth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.os.daviscupindia.network.api.DavisApi
import com.os.daviscupindia.network.api.RetrofitHelper
import com.os.daviscupindia.network.api.safeApiCall
import com.os.daviscupindia.ui.home.model.*

@RequiresApi(Build.VERSION_CODES.N)
class AuthViewModel : ViewModel() {

    val registerResponse = MutableLiveData<RegisterResponse>()

    val verifyResponse = MutableLiveData<RegisterResponse>()

    val resendOTPResponse = MutableLiveData<RegisterResponse>()

    val customerLoginResponse = MutableLiveData<RegisterResponse>()

    val updatePasswordWithOtp = MutableLiveData<RegisterResponse>()

    val forgotPassword = MutableLiveData<RegisterResponse>()

    val error = MutableLiveData<String>()

    var davisApi: DavisApi? = null;

    init {

        davisApi = RetrofitHelper.getInstance().create(DavisApi::class.java)

    }

    suspend fun userRegister(registerRequest: RegisterRequest) {
        if (davisApi == null) {
            davisApi = RetrofitHelper.getInstance().create(DavisApi::class.java)

        }
      safeApiCall {
          val result = davisApi?.userRegister(registerRequest)
          if (result?.body() != null) {
              registerResponse.value = result.body()
          } else
              error.value = result?.errorBody()?.string()
      }
    }


    suspend fun verifyRequest(verifyRequest: VerifyRequest) {
        if (davisApi == null) {
            davisApi = RetrofitHelper.getInstance().create(DavisApi::class.java)

        }
        safeApiCall {
            val result = davisApi?.verifyOTP(verifyRequest)
            if (result?.body() != null) {
                verifyResponse.value = result.body()
            } else
                error.value = result?.errorBody()?.string()
        }
    }

    suspend fun resendOTPRequest(resendOTPRequest: ResendOTPRequest) {
            if (davisApi == null) {
                davisApi = RetrofitHelper.getInstance().create(DavisApi::class.java)

            }
        safeApiCall {
        val result = davisApi?.resendOTP(resendOTPRequest)
            if (result?.body() != null) {
                resendOTPResponse.value = result.body()
            } else
                error.value = result?.errorBody()?.string()
        }


    }


    suspend fun customerLoginRequest(customerLoginRequest: CustomerLoginRequest) {
        if (davisApi == null) {
            davisApi = RetrofitHelper.getInstance().create(DavisApi::class.java)
        }
        safeApiCall {
            val result = davisApi?.customerLogin(customerLoginRequest)
            if (result?.body() != null) {
                customerLoginResponse.value = result.body()
            } else
                error.value = result?.errorBody()?.string()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun forgotPassword(customerLoginRequest: ForgotPassword) {
        if (davisApi == null) {
            davisApi = RetrofitHelper.getInstance().create(DavisApi::class.java)
        }
        safeApiCall {
            val result = davisApi?.forgotPassword(customerLoginRequest)
            if (result?.body() != null) {
                forgotPassword.value = result.body()
            } else
                error.value = result?.errorBody()?.string()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun updatePasswordWithOtp(customerLoginRequest: CustomerLoginRequest) {
        if (davisApi == null) {
            davisApi = RetrofitHelper.getInstance().create(DavisApi::class.java)
        }
        safeApiCall {
            val result = davisApi?.updatePasswordWithOtp(customerLoginRequest)
            if (result?.body() != null) {
                updatePasswordWithOtp.value = result.body()
            } else
                error.value = result?.errorBody()?.string()
        }
    }
}