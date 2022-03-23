package com.os.daviscupindia.utils

import android.app.Application
import android.content.Context
import android.widget.Toast


class MyApplication : Application() {



    override fun onCreate() {
        super.onCreate()

        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        val spPrivate = getSharedPreferences("private", MODE_PRIVATE)
        tinyDB = TinyDB(spPrivate)
        instance = this
        mContext = this

    }
    companion object {
        lateinit var tinyDB: TinyDB
        @get:Synchronized
        var instance: MyApplication? = null
            private set
        var mContext:Context? = null

        fun toast(message: CharSequence)
        {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
        }
    }



}