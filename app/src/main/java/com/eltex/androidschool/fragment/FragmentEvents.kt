package com.eltex.androidschool.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.feature.events.EventNavigationListener
import com.eltex.androidschool.feature.events.EventsScreen
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.theme.ComposeAppTheme
import com.eltex.androidschool.viewmodel.EventViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentEvents : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModel by viewModels<EventViewModel>()

        val eventNavigationListener = object : EventNavigationListener {
            override fun onEditClickListener(dest: Int, event: EventUiModel) {
                requireParentFragment()
                    .requireParentFragment()
                    .findNavController()
                    .navigate(
                        dest,
                        bundleOf(
                            NewEventFragment.ARG_EVENT to event
                        )
                    )
            }

            override fun onEventDetailsClickListener(dest: Int, event: EventUiModel) {
                requireParentFragment()
                    .requireParentFragment()
                    .findNavController()
                    .navigate(
                        dest,
                        bundleOf(
                            EventDetailsFragment.ARG_EVENT to event
                        )
                    )
            }

        }

        val view = ComposeView(requireContext()).apply {
            setContent {
                ComposeAppTheme {
                    EventsScreen(
                        viewModel = viewModel,
                        listener = eventNavigationListener
                    )
                }
            }
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewEventFragment.EVENT_UPDATED,
            viewLifecycleOwner
        )
        { _, _ ->
            viewModel.accept(EventMessage.Refresh)
        }

        return view
    }
}

