package com.eltex.androidschool.fragment.job

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentDateJobBinding
import com.eltex.androidschool.mapper.job.TextWatcherJob
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date


class DateJobFragment : DialogFragment() {

    companion object{
        const val DATE_REQUEST_KEY = "dateRequestKey"
        const val START_DATE_KEY = "startDate"
        const val FINISH_DATE_KEY = "finishDate"
    }

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_date_job, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.RoundedCornersDialog)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onResume() {
        super.onResume()
        val width = resources.displayMetrics.widthPixels * 0.95
        val height = resources.displayMetrics.heightPixels * 0.38
        dialog?.window?.setLayout(width.toInt(), height.toInt())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDateJobBinding.bind(view)

        binding.apply {
            root.setOnClickListener{
                startDate.clearFocus()
                endDate.clearFocus()
            }
            buttonDate.setOnClickListener {
                showDatePickerDialog(binding)
            }
            cancelButton.setOnClickListener {
                dismiss()
            }
            selectButton.setOnClickListener {
                val startDateText = startDate.text.toString()
                val endDateText = endDate.text.toString()
                if (isValidDate(startDateText)) {
                    if (endDateText.isEmpty() || isValidDate(endDateText)) {
                        setFragmentResult(DATE_REQUEST_KEY, Bundle().apply {
                            putString(START_DATE_KEY, startDateText)
                            putString(FINISH_DATE_KEY, endDateText)
                        })
                        dismiss()
                    } else {
                        endDateLayout.error = getString(R.string.invalid_date)
                    }
                } else {
                    startDateLayout.error = getString(R.string.invalid_date)
                }
            }
            startDate.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    startDateLayout.error = null
                }
            }
            endDate.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    endDateLayout.error = null
                }
            }
            startDate.addTextChangedListener(TextWatcherJob(startDateLayout))
            endDate.addTextChangedListener(TextWatcherJob(endDateLayout))
        }
    }

    private fun showDatePickerDialog(binding: FragmentDateJobBinding) {
        binding.buttonDate.isEnabled = false
        val datePickerHelper = DatePickerHelper.getInstance()
        datePickerHelper.showDatePickerDialog(requireActivity())
        datePickerHelper.apply {
            setNegativeButtonClickListener {
                binding.buttonDate.isEnabled = true
            }
            setPositiveButtonClickListener { dateSelection ->
                val startDate = dateSelection.startDate
                val finishDate = dateSelection.endDate

                val formattedStartDate = formatDateToString(startDate)
                val formattedEndDate = formatDateToString(finishDate)

                binding.startDate.setText(formattedStartDate)
                binding.endDate.setText(formattedEndDate)
                binding.buttonDate.isEnabled = true
            }
        }
    }

    fun isValidDate(dateString: String): Boolean {
        dateFormat.isLenient = false
        return try {
            dateFormat.parse(dateString)
            true
        } catch (e: ParseException) {
            false
        }
    }

    fun formatDateToString(dateInMillis: Long): String {
        val date = Date(dateInMillis)
        return dateFormat.format(date)
    }
}


