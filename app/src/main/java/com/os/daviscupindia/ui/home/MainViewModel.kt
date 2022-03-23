package com.os.daviscupindia.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.os.daviscupindia.network.api.DavisApi
import com.os.daviscupindia.network.api.RetrofitHelper
import com.os.daviscupindia.network.api.safeApiCall
import com.os.daviscupindia.ui.home.model.HomeData
import com.os.daviscupindia.ui.home.model.setting.SettingData
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

     val homeDataModel = MutableLiveData<HomeData>();
     val settingData = MutableLiveData<SettingData>();

    init {
        val homeData = RetrofitHelper.getInstance().create(DavisApi::class.java)

        viewModelScope.launch  {
            safeApiCall {
                val result = homeData.getHomePage()
                if (result != null)
                // Checking the results
                    Log.d("ayush: ", result.body().toString())
                homeDataModel.value = result.body()
            }
            safeApiCall {
                val result = homeData.getSetting()
                if (result != null)
                // Checking the results
                    Log.d("ayush: ", result.body().toString())
                settingData.value = result.body()
            }
        }
    }
}