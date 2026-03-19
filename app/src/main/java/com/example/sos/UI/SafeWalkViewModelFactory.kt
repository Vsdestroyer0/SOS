package com.example.sos.UI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sos.data.SafeWalkRepository

class SafeWalkViewModelFactory(private val repository: SafeWalkRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SafeWalkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SafeWalkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}