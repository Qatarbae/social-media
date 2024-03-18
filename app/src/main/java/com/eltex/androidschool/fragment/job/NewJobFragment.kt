package com.eltex.androidschool.fragment.job

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentJobBinding
import com.eltex.androidschool.mapper.job.TextWatcherJob
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.util.getErrorText
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import com.eltex.androidschool.viewmodel.job.NewJobViewModel
import com.eltex.androidschool.viewmodel.job.NewJobViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class NewJobFragment : Fragment() {

    companion object {
        const val ARG_JOB_ID = "ARG_JOB_ID"
        const val REQUEST_KEY = "requestKey"
        const val DATE_REQUEST_KEY = "dateRequestKey"
        const val START_DATE_KEY = "startDate"
        const val FINISH_DATE_KEY = "finishDate"
        const val JOB = "job"
        const val DATE_JOB_FRAGMENT_TAG = "date_job_fragment"
    }

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()

    override fun onStart() {
        super.onStart()
        toolbarViewModel.setSaveVisibility(false)
    }

    override fun onStop() {
        super.onStop()
        toolbarViewModel.setSaveVisibility(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentJobBinding.inflate(layoutInflater)

        val id = arguments?.getLong(ARG_JOB_ID) ?: 0L

        val viewModel by viewModels<NewJobViewModel>(
            extrasProducer = {
                defaultViewModelCreationExtras
                    .withCreationCallback<NewJobViewModelFactory> { factory ->
                        factory.create(id)
                    }
            }
        )

        var finishDate = ""
        var startDate = ""
        viewModel.state.onEach { state ->
            (state.status as? Status.Error)?.let {
                Toast.makeText(
                    requireContext(),
                    it.reason.getErrorText(requireContext()),
                    Toast.LENGTH_SHORT
                ).show()

                viewModel.consumeError()
            }
            state.result?.let {
                requireActivity().supportFragmentManager
                    .setFragmentResult(REQUEST_KEY,
                        bundleOf().apply {
                            putParcelable(JOB, it)
                        })

                findNavController().navigateUp()
            }
        }
            .launchIn(viewLifecycleOwner.lifecycleScope)


        binding.root.setOnClickListener {
            clearFocusFromFields(binding)
        }
        requireActivity().supportFragmentManager.setFragmentResultListener(
            DATE_REQUEST_KEY, this
        ) { requestKey, bundle ->
            startDate = bundle.getString(START_DATE_KEY).toString()
            finishDate = bundle.getString(FINISH_DATE_KEY).toString()
            if (finishDate.isEmpty()){
                binding.date.setText("$startDate - ${R.string.empty_end_date}")
            }
            else {
                binding.date.setText("$startDate - $finishDate")
            }

        }
        binding.apply {
            nameJobLayout.setEndIconOnClickListener {
                nameJob.text = null
            }
            positionJobLayout.setEndIconOnClickListener {
                positionJob.text = null
            }
            linkLayout.setEndIconOnClickListener {
                link.text = null
            }

            nameJob.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    nameJobLayout.error = null
                }
            }

            positionJob.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    positionJobLayout.error = null
                }
            }

            link.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    linkLayout.error = null
                }
            }
            nameJobLayout.isEndIconVisible = false
            positionJobLayout.isEndIconVisible = false
            linkLayout.isEndIconVisible = false


            nameJob.addTextChangedListener(TextWatcherJob(nameJobLayout))
            positionJob.addTextChangedListener(TextWatcherJob(positionJobLayout))
            link.addTextChangedListener(TextWatcherJob(linkLayout))

            date.setOnClickListener {
                date.error = null
                val date = DateJobFragment()
                date.show(requireActivity().supportFragmentManager, DATE_JOB_FRAGMENT_TAG)
            }

            buttonCreateJob.setOnClickListener {
                if (validateInputs(startDate)) {
                    viewModel.savePost(
                        nameJob.text.toString(),
                        positionJob.text.toString(),
                        startDate,
                        finishDate,
                        link.text.toString()
                    )
                }
                clearFocusFromFields(binding)
            }
        }
        return binding.root
    }
    private fun clearFocusFromFields(binding: FragmentJobBinding) {
        binding.nameJob.clearFocus()
        binding.positionJob.clearFocus()
        binding.link.clearFocus()
    }
    private fun validateInputs(start: String): Boolean {
        val binding = FragmentJobBinding.bind(requireView())

        val name = binding.nameJob.text
        val position = binding.positionJob.text
        val link = binding.link.text

        var isValid = true

        if (name.isNullOrEmpty()) {
            binding.nameJobLayout.error = getString(R.string.error_name_empty)
            isValid = false
        } else {
            binding.nameJobLayout.error = null
        }

        if (position.isNullOrEmpty()) {
            binding.positionJobLayout.error = getString(R.string.error_position_empty)
            isValid = false
        } else {
            binding.positionJobLayout.error = null
        }

        if (!link.isNullOrEmpty() && !URLUtil.isValidUrl(link.toString())) {
            binding.linkLayout.error = getString(R.string.error_invalid_link)
            isValid = false
        } else {
            binding.linkLayout.error = null
        }
        if(start.isEmpty()){
            binding.date.error = getString(R.string.data_cannot_be_empty)
            isValid = false
        }
        else{
            binding.date.error = null
        }

        return isValid
    }
}