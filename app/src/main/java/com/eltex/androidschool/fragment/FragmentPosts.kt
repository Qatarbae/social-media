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
import com.eltex.androidschool.R
import com.eltex.androidschool.feature.comments.fragment.CommentFragment
import com.eltex.androidschool.feature.posts.PostScreen
import com.eltex.androidschool.model.PostMessage
import com.eltex.androidschool.theme.ComposeAppTheme
import com.eltex.androidschool.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentPosts : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val viewModel by viewModels<PostViewModel>()

        val view = ComposeView(requireContext()).apply {
            setContent {
                ComposeAppTheme {
                    PostScreen(
                        viewModel = viewModel,
                        onCommentClicked = { post ->
                            requireParentFragment()
                                .requireParentFragment()
                                .findNavController().navigate(
                                    R.id.action_bottomMenuFragment_to_commentFragment,
                                    bundleOf(
                                        CommentFragment.POST_ID_VALUE to post.id
                                    ),
                                )
                        })
                }
            }
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewPostFragment.POST_CREATED_RESULT,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.accept(PostMessage.Refresh)
        }

        return view
    }
}
