package com.eltex.androidschool.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

abstract class AbstractTextWatcher(
    private val editText: EditText,
    private val inputLayout: TextInputLayout,
) : TextWatcher {

    override fun afterTextChanged(s: Editable) {
        validate(editText, editText.text.toString().trim(), inputLayout)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) =
        Unit

    abstract fun validate(editText: EditText, text: String, inputLayout: TextInputLayout)

}