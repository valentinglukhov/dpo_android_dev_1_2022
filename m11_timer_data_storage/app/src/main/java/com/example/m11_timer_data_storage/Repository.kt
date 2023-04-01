package com.example.m11_timer_data_storage

import android.content.Context
import android.content.Context.MODE_PRIVATE

private const val PREFERENCE = "preference"
private const val STRING_KEY = "variable"

class Repository (context: Context) {

    var localString: String? = null
    val preference = context.getSharedPreferences(PREFERENCE, MODE_PRIVATE)

    fun getText(): String {
        return when {
            getDataFromLocalVariable() != null -> getDataFromLocalVariable()!!
            getDataFromSharedPreference() != null -> getDataFromSharedPreference()!!
            else -> ""
        }
    }

    fun getDataFromSharedPreference(): String? {
        val sharedPrefString: String? = preference.getString(STRING_KEY, null)
        return sharedPrefString
    }

    fun getDataFromLocalVariable(): String? {
        return localString
    }

    fun saveText(text: String) {
        localString = text
        preference.edit().putString(STRING_KEY, text).apply()
    }

    fun clearText() {
        localString = null
        preference.edit().clear().apply()
    }
}