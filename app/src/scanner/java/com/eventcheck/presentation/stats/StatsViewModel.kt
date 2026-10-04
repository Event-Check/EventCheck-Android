package com.eventcheck.presentation.stats

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventcheck.R
import com.eventcheck.data.DataException
import com.eventcheck.data.EventDatasource
import com.eventcheck.data.response.StatsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val eventDatasource: EventDatasource
) : ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _stats = mutableStateOf<StatsResponse?>(null)
    val stats: State<StatsResponse?> = _stats

    private val _errorResId = mutableStateOf<Int?>(null)
    val errorResId: State<Int?> = _errorResId

    init {
        fetchStats()
    }

    fun fetchStats() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorResId.value = null
            try {
                val response = eventDatasource.getStats()
                _stats.value = response
            } catch (e: DataException) {
                if (e is DataException.NetworkError) {
                    _errorResId.value = R.string.error_network
                } else {
                    _stats.value = null
                }
            } catch (_: Exception) {
                _stats.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorResId.value = null
    }
}
