package com.eltex.androidschool.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentNewPostBinding
import com.eltex.androidschool.feature.users.UsersArguments
import com.eltex.androidschool.model.AttachmentType
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.util.UriProvider
import com.eltex.androidschool.util.getText
import com.eltex.androidschool.viewmodel.LocationViewModel
import com.eltex.androidschool.viewmodel.NewPostViewModel
import com.eltex.androidschool.viewmodel.NewPostViewModelFactory
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class NewPostFragment : Fragment() {

    private val checkBoxOn = UsersArguments(chooser = true)

    private var selectMentionIds: List<Long>? = null

    companion object {
        const val ARG_POST_ID = "ARG_POST_ID"
        const val POST_CREATED_RESULT = "POST_CREATED_RESULT"
    }

    @Inject
    lateinit var uriProvider: UriProvider

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()
    private val locationViewModel by activityViewModels<LocationViewModel>()

    override fun onStart() {
        super.onStart()
        toolbarViewModel.setSaveVisibility(true)
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
        val binding = FragmentNewPostBinding.inflate(layoutInflater)

        val id = arguments?.getLong(ARG_POST_ID) ?: 0L

        val viewModel by viewModels<NewPostViewModel>(
            extrasProducer = {
                defaultViewModelCreationExtras.withCreationCallback<NewPostViewModelFactory> { factory ->
                    factory.create(id)
                }
            }
        )

        viewModel.state.onEach { state ->
            (state.status as? Status.Error)?.let {
                Toast.makeText(
                    requireContext(),
                    it.reason.getText(requireContext()),
                    Toast.LENGTH_SHORT
                ).show()

                viewModel.consumeError()
            }

            val fileModel = state.file
            when (fileModel?.type) {
                AttachmentType.IMAGE -> with(binding) {
                    preview.isVisible = true

                    video.isVisible = false
                    videoPlaceholder.isVisible = false
                    playPauseButton.isVisible = false
                    audioFile.isVisible = false

                    photoPreview.isVisible = true
                    photoPreview.setImageURI(fileModel.uri.toUri())
                }

                AttachmentType.VIDEO -> with(binding) {
                    preview.isVisible = true

                    photoPreview.isVisible = false
                    audioFile.isVisible = false

                    video.isVisible = true
                    video.setVideoURI(fileModel.uri.toUri())
                    playPauseButton.isVisible = true
                    videoPlaceholder.isVisible = true
                    playPauseButton.setOnClickListener {
                        if (video.isPlaying) {
                            video.pause()
                        } else {
                            videoPlaceholder.isVisible = false
                            video.start()
                        }
                    }
                }

                AttachmentType.AUDIO -> with(binding) {
                    preview.isVisible = true

                    photoPreview.isVisible = false
                    video.isVisible = false
                    videoPlaceholder.isVisible = false
                    playPauseButton.isVisible = false

                    audioFile.isVisible = true
                }

                null -> with(binding) {
                    preview.isGone = true
                    videoPlaceholder.isVisible = true
                    audioFile.isVisible = true
                }
            }

            state.result?.let {
                requireActivity().supportFragmentManager.setFragmentResult(
                    POST_CREATED_RESULT,
                    bundleOf()
                )
                findNavController().navigateUp()
            }
        }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        val gallery =
            registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
                result?.let {
                    val mimeType = requireActivity().contentResolver.getType(it)
                    val attachmentType = getAttachmentType(mimeType)
                    viewModel.setFile(FileModel(it.toString(), attachmentType))
                }
            }

        binding.gallery.setOnClickListener {
            val dialogFragment = AttachmentTypeDialogFragment()
            dialogFragment.show(parentFragmentManager, null)

            setFragmentResultListener("requestKey") { _, bundle ->
                val result = bundle.getString("mimeType")
                gallery.launch(result)
            }
        }

        val photoUri = uriProvider.getPhotoUri()
        val takePicture =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    viewModel.setFile(FileModel(photoUri.toString(), AttachmentType.IMAGE))
                }
            }

        val videoUri = uriProvider.getVideoUri()
        val takeVideo =
            registerForActivityResult(ActivityResultContracts.CaptureVideo()) { success ->
                if (success) {
                    viewModel.setFile(FileModel(videoUri.toString(), AttachmentType.VIDEO))
                }
            }

        binding.takePhoto.setOnClickListener {
            val options = arrayOf(getString(R.string.photocamera), getString(R.string.videocamera))

            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.select_camera))
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> takePicture.launch(photoUri)
                        1 -> takeVideo.launch(videoUri)
                    }
                }
                .show()

            binding.playPauseButton.isVisible = true
            binding.videoPlaceholder.isVisible = true
        }

        binding.remove.setOnClickListener {
            viewModel.setFile(null)
            binding.video.pause()
        }

        binding.peopleOutline.setOnClickListener {
            val args = bundleOf("ARGS_KEY" to checkBoxOn)
            findNavController().navigate(
                R.id.action_newPostFragment_to_chooseUserFragment, args
            )

            setFragmentResultListener("FILTER_RESULT_KEY") { _, bundle ->
                val filterResult = bundle.getLongArray("FILTER_BUNDLE_KEY")

                selectMentionIds = filterResult?.toList() ?: emptyList()
                viewModel.setMentionIds(requireNotNull(selectMentionIds))
            }
        }

        binding.location.setOnClickListener {
            findNavController().navigate(R.id.action_newPostFragment_to_locationFragment)
        }

        binding.audioRemove.setOnClickListener {
            viewModel.setFile(null)
        }

        val toolbarViewModel by activityViewModels<ToolbarViewModel>()
        toolbarViewModel.saveClicked
            .filter { it }
            .onEach {
                val content = binding.content.text?.toString().orEmpty()

                if (content.isBlank()) {
                    Toast.makeText(requireContext(), R.string.empty_post_error, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.setCoordinates(locationViewModel.selectedCoordinates.value)
                    viewModel.savePost(content)
                }

                toolbarViewModel.saveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

    private fun getAttachmentType(mimeType: String?): AttachmentType = when {
        mimeType?.startsWith("image") == true -> AttachmentType.IMAGE
        mimeType?.startsWith("video") == true -> AttachmentType.VIDEO
        mimeType?.startsWith("audio") == true -> AttachmentType.AUDIO
        else -> AttachmentType.IMAGE
    }
}
