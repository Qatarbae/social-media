package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ToolbarViewModel : ViewModel() {
    private val _showSave = MutableStateFlow(false)
    val showSave = _showSave.asStateFlow()

    private val _saveClicked = MutableStateFlow(false)
    val saveClicked = _saveClicked.asStateFlow()

    private val _shareIcon = MutableStateFlow(false)
    val shareIcon = _shareIcon.asStateFlow()

    fun setSaveVisibility(visible: Boolean) {
        _showSave.value = visible
    }

    fun saveClicked(pending: Boolean) {
        _saveClicked.value = pending
    }

    fun isShareIcon(isShare: Boolean) {
        _shareIcon.value = isShare
    }
}
