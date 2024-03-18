package com.eltex.androidschool.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.AvatarAdapter
import com.eltex.androidschool.databinding.FragmentEventDetailsBinding
import com.eltex.androidschool.itemdecoration.OffsetDecoration
import com.eltex.androidschool.model.AttachmentType
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.util.getText
import com.eltex.androidschool.viewmodel.EventDetailsViewModel
import com.eltex.androidschool.viewmodel.EventDetailsViewModelFactory
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class EventDetailsFragment : Fragment() {
    companion object {
        const val ARG_EVENT = "ARG_EVENT"
        const val EVENT_UPDATED = "EVENT_UPDATED"
    }

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()
    private var mapView: MapView? = null
    override fun onStart() {
        super.onStart()
        toolbarViewModel.isShareIcon(true)
        toolbarViewModel.setSaveVisibility(true)

        mapView?.onStart()
    }

    override fun onStop() {
        toolbarViewModel.setSaveVisibility(false)
        toolbarViewModel.isShareIcon(false)

        mapView?.onStop()
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        val event: EventUiModel = requireNotNull(arguments?.getParcelable(ARG_EVENT))

        mapView = binding.map

        if (event.coords != null) {
            val map = requireNotNull(mapView?.mapWindow?.map)
            map.move(
                CameraPosition(
                    Point(event.coords.lat, event.coords.long),
                    17.0f, 150.0f, 30.0f
                )
            )

            val imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.map_pin)
            map.mapObjects.addPlacemark().apply {
                geometry = Point(event.coords.lat, event.coords.long)
                setIcon(imageProvider)
            }
        } else {
            mapView?.isVisible = false
        }

        val speakersAdapter = AvatarAdapter(
            object : AvatarAdapter.AvatarListener {
                override fun onItemsClickListener() {
                    //TODO("Not yet implemented")
                }

            }
        )
        binding.speakersList.adapter = speakersAdapter
        binding.speakersList.addItemDecoration(
            OffsetDecoration(
                offsetLeft = -20,
                orientation = LinearLayout.HORIZONTAL
            )
        )

        val likersAdapter = AvatarAdapter(
            object : AvatarAdapter.AvatarListener {
                override fun onItemsClickListener() {
                    //TODO("Not yet implemented")
                }
            }
        )
        binding.likersList.adapter = likersAdapter
        binding.likersList.addItemDecoration(
            OffsetDecoration(
                offsetLeft = -20,
                orientation = LinearLayout.HORIZONTAL
            )
        )

        val particpantsAdapter = AvatarAdapter(
            object : AvatarAdapter.AvatarListener {
                override fun onItemsClickListener() {
                    //TODO("Not yet implemented")
                }
            }
        )
        binding.participantsList.adapter = particpantsAdapter
        binding.participantsList.addItemDecoration(
            OffsetDecoration(
                offsetLeft = -20,
                orientation = LinearLayout.HORIZONTAL
            )
        )

        val viewModel by viewModels<EventDetailsViewModel>(
            extrasProducer = {
                defaultViewModelCreationExtras.withCreationCallback<EventDetailsViewModelFactory> { factory ->
                    factory.create(event)
                }
            }
        )

        if (event.authorAvatar != null) {
            binding.initial.isGone = true
            Glide.with(binding.avatar)
                .load(event.authorAvatar)
                .transform(CircleCrop())
                .into(binding.avatar)
        } else {
            binding.avatar.setImageResource(R.drawable.avatar_background)
            binding.initial.isVisible = true
            binding.initial.text = event.author.take(1)
        }

        binding.author.text = event.author
        binding.authorJob.text = event.authorJob
        if (event.attachment != null) {
            when (event.attachment.attachmentType) {
                AttachmentType.IMAGE -> Glide.with(binding.attach)
                    .load(event.attachment.url)
                    .into(binding.attach)

                // TODO обработать другие типы вложений
                AttachmentType.VIDEO,
                AttachmentType.AUDIO -> {}
            }
        }
        binding.eventType.text = event.type
        binding.eventDate.text = event.datetime
        binding.content.text = event.content

        speakersAdapter.submitList(event.speakersList)

        updateLike(binding, event)
        likersAdapter.submitList(event.likersList)

        updateParticipate(binding, event)
        particpantsAdapter.submitList(event.participantsList)

        viewModel.state.onEach { state ->

            (state.status as? Status.Error)?.let {
                Toast.makeText(
                    requireContext(),
                    it.reason.getText(requireContext()),
                    Toast.LENGTH_SHORT
                ).show()

                viewModel.handleError()
            }

            if (state.isLikePressed) {
                val stateEvent = requireNotNull(state.event)
                updateLike(binding, stateEvent)
                likersAdapter.submitList(stateEvent.likersList)
            }

            if (state.isParticipatePressed) {
                val stateEvent = requireNotNull(state.event)
                updateParticipate(binding, stateEvent)
                particpantsAdapter.submitList(stateEvent.participantsList)
            }

        }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.like.setOnClickListener {
            viewModel.like()
        }

        binding.participation.setOnClickListener {
            viewModel.participate()
        }

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val state = viewModel.state.value
                if (state.isLikePressed || state.isParticipatePressed) {
                    requireActivity().supportFragmentManager.setFragmentResult(
                        EVENT_UPDATED,
                        bundleOf()
                    )
                }
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        toolbarViewModel.saveClicked
            .filter { it }
            .onEach {
                val intent = Intent()
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, event.content)
                    .setType("text/plain")

                val chooser = Intent.createChooser(intent, null)
                requireContext().startActivity(chooser)

                toolbarViewModel.saveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

    private fun updateLike(
        binding: FragmentEventDetailsBinding,
        event: EventUiModel
    ) {
        binding.like.icon =
            ContextCompat.getDrawable(
                requireContext(),
                if (event.likedByMe) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )
        binding.like.text = event.likes.toString()
    }

    private fun updateParticipate(
        binding: FragmentEventDetailsBinding,
        event: EventUiModel
    ) {
        binding.participation.icon =
            ContextCompat.getDrawable(
                requireContext(),
                if (event.participatedByMe) R.drawable.baseline_people_24
                else R.drawable.baseline_people_outline_24
            )
        binding.participation.text = event.participants.toString()
    }
}
