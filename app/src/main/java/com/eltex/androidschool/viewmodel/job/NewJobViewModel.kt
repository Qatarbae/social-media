package com.eltex.androidschool.viewmodel.job

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.mapper.job.DateTimeJobFormatter
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.model.job.MyJob
import com.eltex.androidschool.repository.job.JobRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

@HiltViewModel(assistedFactory = NewJobViewModelFactory::class)
class NewJobViewModel @AssistedInject constructor(
    private val dateTimeJobFormatter: DateTimeJobFormatter,
    private val repository: JobRepository,
    @Assisted
    private val jobId: Long = 0,
) : ViewModel() {

    private val _state = MutableStateFlow(NewJobUiState())
    val state = _state.asStateFlow()

    fun savePost(
        name: String,
        position: String,
        start: String,
        finish: String?,
        link: String?,
    ) {
        viewModelScope.launch {
            try {
                val myJob = repository.saveJob(
                    jobId,
                    MyJob(
                        name = name,
                        position = position,
                        start = dateTimeJobFormatter.parseDate(start)?: Instant.now(),
                        finish = dateTimeJobFormatter.parseDate(finish?:""),
                        link = link
                    )
                )
                _state.update { it.copy(result = myJob, status = Status.Idle) }
            } catch (e: Exception) {
                _state.update { it.copy(status = Status.Error(e)) }
            }
        }
    }


    fun consumeError() {
        _state.update { it.copy(status = Status.Idle) }
    }
}