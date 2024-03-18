package com.eltex.androidschool.feature.comments.store

import com.eltex.androidschool.feature.comments.model.CommentEffect
import com.eltex.androidschool.feature.comments.model.CommentMessage
import com.eltex.androidschool.feature.comments.viewmodel.CommentUiState
import com.eltex.androidschool.mvi.Store

typealias CommentStore = Store<CommentUiState, CommentMessage, CommentEffect>