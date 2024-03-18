package com.eltex.androidschool.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.feature.users.UserScreen
import com.eltex.androidschool.feature.users.UsersArguments
import com.eltex.androidschool.theme.ComposeAppTheme
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import com.eltex.androidschool.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FragmentUsers : Fragment() {
    companion object {
        const val FILTER_RESULT_KEY = "FILTER_RESULT_KEY"
        const val FILTER_BUNDLE_KEY = "FILTER_BUNDLE_KEY"
        private const val ARGS_KEY = "ARGS_KEY"

        fun createArgs(args: UsersArguments) = bundleOf(ARGS_KEY to args)
    }

    @Suppress("DEPRECATION")
    private val args: UsersArguments
        get() = arguments?.getParcelable(ARGS_KEY) ?: UsersArguments()

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()

    override fun onStart() {
        super.onStart()
        if (args.chooser) {
            toolbarViewModel.setSaveVisibility(true)
        }
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
        val viewModel by viewModels<UserViewModel>(
            extrasProducer = {
                defaultViewModelCreationExtras.withCreationCallback<UserViewModel.Factory> {
                    it.create(args.filterIds)
                }
            }
        )
        val view = ComposeView(requireContext()).apply {
            setContent {
                ComposeAppTheme {
                    UserScreen(viewModel = viewModel, checkable = args.chooser)
                }
            }
        }

        toolbarViewModel.saveClicked
            .filter { it }
            .onEach {
                val result = viewModel.uiState.value.users
                    .filter { it.checked }
                    .map { it.id }

                setFragmentResult(
                    FILTER_RESULT_KEY,
                    bundleOf(FILTER_BUNDLE_KEY to result.toLongArray())
                )

                toolbarViewModel.saveClicked(false)

                findNavController().navigateUp()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return view
    }
}
