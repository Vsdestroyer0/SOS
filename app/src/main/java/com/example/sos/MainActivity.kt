package com.example.sos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos el repositorio y la fábrica del ViewModel
        val repository = SafeWalkRepository(this)

        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Instanciamos el ViewModel pasando el repositorio
                    val viewModel: SafeWalkViewModel = viewModel(
                        factory = SafeWalkViewModelFactory(repository)
                    )

                    // Llamamos a la interfaz que creamos antes
                    ConfigScreen(viewModel = viewModel)
                }
            }
        }
    }
}