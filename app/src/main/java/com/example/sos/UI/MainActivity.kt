package com.example.sos.UI

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sos.UI.SafeWalkViewModelFactory
import com.example.sos.data.SafeWalkRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = SafeWalkRepository(this)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val viewModel: SafeWalkViewModel = viewModel(
                        factory = SafeWalkViewModelFactory(repository)
                    )
                    ConfigScreen(viewModel = viewModel)
                }
            }
        }
    }
}