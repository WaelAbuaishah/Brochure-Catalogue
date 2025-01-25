package com.android.brochurecatalogue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.brochurecatalogue.brochureslist.BrochureViewModel
import com.android.brochurecatalogue.brochureslist.BrochuresListScreen
import com.android.brochurecatalogue.ui.theme.BrochureCatalogueTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BrochureCatalogueTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: BrochureViewModel = viewModel()
                    BrochuresListScreen(Modifier.padding(innerPadding), viewModel)
                }
            }
        }
    }
}