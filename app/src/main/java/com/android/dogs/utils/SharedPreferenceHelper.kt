package com.android.dogs.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

/* This is SharedPreferenceHelper class which is used to store and get data from SharedPreference.
*  This is implemented using Singleton design pattern.
* */
class SharedPreferenceHelper {

    companion object {

        private var prefs: SharedPreferences? = null

        @Volatile
        private var instance: SharedPreferenceHelper? = null
        private var LOCK = Any()

        operator fun invoke(context: Context): SharedPreferenceHelper =
            instance ?: synchronized(LOCK) {
                instance ?: buildHelper(context).also {
                    instance = it
                }
            }

        private fun buildHelper(context: Context): SharedPreferenceHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferenceHelper()
        }

    }

    fun saveUpdateTime(time: Long) {
        prefs?.edit(commit = true) {
            putLong(PREF_TIME, time)
        }
    }

    fun getUpdateTime() = prefs?.getLong(PREF_TIME,0)

    fun getCacheDuration () = prefs?.getString(PREF_CACHE_DURATION,"")
}