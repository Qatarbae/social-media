package com.eltex.androidschool.mapper.job

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Locale

class TextWatcherJob(private val layout: TextInputLayout) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        layout.isEndIconVisible = !s.isNullOrEmpty()
        layout.error
    }

    override fun afterTextChanged(s: Editable?) {
    }
}