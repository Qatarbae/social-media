package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Coordinates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor() : ViewModel() {
    private val _selectedCoordinates = MutableStateFlow<Coordinates?>(null)
    val selectedCoordinates = _selectedCoordinates.asStateFlow()

    fun setSelectedCoordinates(coordinates: Coordinates?) {
        _selectedCoordinates.value = coordinates
    }
}
