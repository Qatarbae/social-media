package com.eltex.androidschool.util

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(res: String, short: Boolean = true) {
    val len: Int = when (short) {
        true -> Toast.LENGTH_SHORT
        false -> Toast.LENGTH_LONG
    }
    Toast.makeText(requireContext(), res, len).show()
}