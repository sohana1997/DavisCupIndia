package com.os.daviscupindia.utils

import com.os.daviscupindia.utils.MyApplication.Companion.toast
import org.json.JSONException
import org.json.JSONObject

fun handleApiError(
    message: String,
) {

    try {
        val jsonObject = JSONObject(message)
        val error = jsonObject.getString("message")
        toast(error)

    } catch (err: JSONException) {
        val error = "Bad Request or No Internet"
        toast(error)
        ProgressDialog.hideProgressDialog()
        // snackbar(error, view)
    }


}