package com.eltex.androidschool.feature.comments.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eltex.androidschool.feature.comments.CommentsScreen
import com.eltex.androidschool.feature.comments.viewmodel.CommentViewModel
import com.eltex.androidschool.theme.ComposeAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CommentFragment : Fragment() {

    companion object {
        const val POST_ID_VALUE = "POST_ID_VALUE"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val postId = arguments?.getLong(POST_ID_VALUE)

        val viewModel by viewModels<CommentViewModel>()

        val view = ComposeView(requireContext()).apply {
            setContent {
                ComposeAppTheme {
                    CommentsScreen(viewModel = viewModel, postId = postId)
                }
            }
        }

        return view
    }
}