package com.eltex.androidschool.util

import android.widget.EditText
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentRegistrationBinding
import com.eltex.androidschool.viewmodel.RegistrationViewModel
import com.google.android.material.textfield.TextInputLayout

fun EditText.validatePasswordOnChangedText(
    inputLayout: TextInputLayout, password: TextInputLayout, binding: FragmentRegistrationBinding,
    viewModel: RegistrationViewModel,
) {
    this.addTextChangedListener(object : AbstractTextWatcher(this, inputLayout) {
        override fun validate(editText: EditText, text: String, inputLayout: TextInputLayout) {
            if (text != password.editText?.text.toString()) {
                inputLayout.isErrorEnabled = true
                inputLayout.error = context.getString(R.string.password_mismatch)
                if (text.isEmpty()) inputLayout.error =
                    inputLayout.hint.toString() + context.getString(R.string.cannot_be_empty)
            } else {
                inputLayout.isErrorEnabled = false
                password.isErrorEnabled = false
            }
            registryValidation(binding, viewModel)
        }
    })
}

fun EditText.validateLoginAndNameOnChangedText(
    inputLayout: TextInputLayout, binding: FragmentRegistrationBinding,
    viewModel: RegistrationViewModel,
) {
    this.addTextChangedListener(object : AbstractTextWatcher(this, inputLayout) {
        override fun validate(editText: EditText, text: String, inputLayout: TextInputLayout) {
            if (text.isEmpty()) {
                inputLayout.isErrorEnabled = true
                inputLayout.error =
                    inputLayout.hint.toString() + context.getString(R.string.cannot_be_empty)
            } else {
                inputLayout.isErrorEnabled = false
            }
            registryValidation(binding, viewModel)
        }
    })
}

fun EditText.validatePasswordAfterChangedFocus(
    inputLayout: TextInputLayout, binding: FragmentRegistrationBinding,
    viewModel: RegistrationViewModel,
) {
    this.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            if (this.text.toString().isEmpty()) {
                inputLayout.isErrorEnabled = true
                inputLayout.error =
                    inputLayout.hint.toString() + context.getString(R.string.cannot_be_empty)
            }
        }
    }
    registryValidation(binding, viewModel)
}

fun EditText.validateLoginAndNameAfterChangedFocus(
    inputLayout: TextInputLayout, binding: FragmentRegistrationBinding,
    viewModel: RegistrationViewModel,
) {
    this.setOnFocusChangeListener { _, hasFocus ->
        if (this.text.toString().isEmpty()) {
            inputLayout.isErrorEnabled = true
            inputLayout.error =
                inputLayout.hint.toString() + context.getString(R.string.cannot_be_empty)
        } else {
            inputLayout.isErrorEnabled = false
        }
        if (!hasFocus) {
            inputLayout.endIconMode = TextInputLayout.END_ICON_NONE
        } else {
            inputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
        }
    }
    registryValidation(binding, viewModel)
}


fun registryValidation(
    binding: FragmentRegistrationBinding,
    viewModel: RegistrationViewModel,
) {
    val validator = (with(binding) {
        !login.isErrorEnabled && !name.isErrorEnabled && !password.isErrorEnabled
                && !repeatPassword.isErrorEnabled
                && name.editText?.text.toString().trim().isNotEmpty()
                && login.editText?.text.toString().trim().isNotEmpty()
                && password.editText?.text.toString().trim().isNotEmpty()
                && repeatPassword.editText?.text.toString().trim().isNotEmpty()
    })
    viewModel.validate(validator)

}

