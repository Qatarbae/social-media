package com.eltex.androidschool.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentNewEventBinding
import com.eltex.androidschool.model.AttachmentType
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.util.UriProvider
import com.eltex.androidschool.util.getText
import com.eltex.androidschool.util.toast
import com.eltex.androidschool.viewmodel.NewEventViewModel
import com.eltex.androidschool.viewmodel.NewEventViewModelFactory
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
class NewEventFragment : Fragment() {

    companion object {
        const val ARG_EVENT = "ARG_EVENT"
        const val EVENT_UPDATED = "EVENT_UPDATED"
    }

    @Inject
    lateinit var uriProvider: UriProvider

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()

    override fun onStart() {
        super.onStart()
        toolbarViewModel.setSaveVisibility(true)
    }

    override fun onStop() {
        super.onStop()
        toolbarViewModel.setSaveVisibility(false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewEventBinding.inflate(inflater, container, false)

        val event = arguments?.getParcelable(ARG_EVENT, EventUiModel::class.java)

        if (event?.content?.isNotBlank() == true) {
            binding.content.setText(event.content)
        }

        val id = event?.id ?: 0L

        val viewModel by viewModels<NewEventViewModel>(
            extrasProducer = {
                defaultViewModelCreationExtras.withCreationCallback<NewEventViewModelFactory> { factory ->
                    factory.create(id)
                }
            }
        )

        event?.attachment?.let {attach->
            when (attach.attachmentType) {
                AttachmentType.IMAGE -> viewModel.saveFile(FileModel(attach.url, attach.attachmentType, true))
                AttachmentType.VIDEO,
                AttachmentType.AUDIO -> TODO()
            }
        }

        event?.coords?.let {
            viewModel.saveCoords(it)
        }

        viewModel.state.onEach { state ->
            (state.status as? Status.Error)?.let {
                Toast.makeText(
                    requireContext(),
                    it.reason.getText(requireContext()),
                    Toast.LENGTH_SHORT
                ).show()

                viewModel.handleError()
            }

            val file = state.file
            when (file?.type) {
                AttachmentType.IMAGE -> {
                    binding.preview.isVisible = true
                    Glide.with(binding.photoPreview)
                        .load(file.uri)
                        .into(binding.photoPreview)
                }

                AttachmentType.VIDEO,
                AttachmentType.AUDIO -> {
                    binding.audioPreview.isVisible = true
                }

                null,
                -> {
                    binding.preview.isGone = true
                    binding.audioPreview.isGone = true
                }
            }

            state.result?.let {
                requireActivity().supportFragmentManager.setFragmentResult(
                    EVENT_UPDATED,
                    bundleOf()
                )
                findNavController().navigateUp()
            }
        }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        val gallery =
            registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
                result?.let {
                    viewModel.saveFile(FileModel(it.toString(), AttachmentType.IMAGE))
                }
            }

        binding.gallery.setOnClickListener {
            gallery.launch("image/*")
        }

        val photoUri = uriProvider.getPhotoUri()
        val takePicture =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    viewModel.saveFile(FileModel(photoUri.toString(), AttachmentType.IMAGE))
                }
            }

        binding.takePhoto.setOnClickListener {
            takePicture.launch(photoUri)
        }

        binding.remove.setOnClickListener {
            viewModel.saveFile(null)
        }

        toolbarViewModel.saveClicked
            .filter { it }
            .onEach {
                val content = binding.content.text?.toString().orEmpty()

                if (content.isNotBlank()) {
                    viewModel.save(content)
                } else {
                    requireContext().toast(R.string.empty_post_error)
                }

                toolbarViewModel.saveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}