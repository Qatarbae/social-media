package com.eltex.androidschool.fragment.job

import androidx.core.util.Pair
import androidx.fragment.app.FragmentActivity
import com.eltex.androidschool.model.job.DateRange
import com.google.android.material.datepicker.MaterialDatePicker

class DatePickerHelper private constructor() {

    private var positiveButtonClickListener: ((DateRange) -> Unit)? = null
    private var negativeButtonClickListener: (() -> Unit)? = null

    fun showDatePickerDialog(activityContext: FragmentActivity) {
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select Date")
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

        dateRangePicker.show(activityContext.supportFragmentManager, "DATE_PICKER_TAG")

        dateRangePicker.addOnPositiveButtonClickListener { dateSelection ->
            val startDate = dateSelection.first ?: return@addOnPositiveButtonClickListener
            val endDate = dateSelection.second ?: return@addOnPositiveButtonClickListener
            positiveButtonClickListener?.invoke(DateRange(startDate, endDate))
        }
    }

    fun setPositiveButtonClickListener(listener: (DateRange) -> Unit) {
        positiveButtonClickListener = listener
    }

    fun setNegativeButtonClickListener(listener: () -> Unit) {
        negativeButtonClickListener = listener
    }

    companion object {
        fun getInstance(): DatePickerHelper {
            return DatePickerHelper()
        }
    }
}