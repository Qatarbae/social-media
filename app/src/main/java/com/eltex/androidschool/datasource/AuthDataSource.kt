package com.eltex.androidschool.datasource

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    companion object {
        const val USER_TOKEN = "USER_TOKEN"
        const val USER_ID = "USER_ID"
    }

    private val prefs = context.getSharedPreferences("token", Context.MODE_PRIVATE)
    var token: String
        get() = prefs.getString(USER_TOKEN, "") ?: ""
        set(value) = prefs.edit {
            putString(USER_TOKEN, value)
        }

    var userId: Long
        get() = prefs.getLong(USER_ID, 0L)
        set(value) = prefs.edit {
            putLong(USER_ID, value)
        }
}