package com.example.cafe.ui.screens.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cafe.utils.SharedPref
import com.example.cafe.viewmodel.AuthViewModel

@Composable
fun Profile(
    authViewModel: AuthViewModel = viewModel(),
    navigateToChoice: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val context = LocalContext.current
        Text(text = "Hello, ${SharedPref.getName(context)}")
        Text(text = "is Customer: ${SharedPref.getIsCustomer(context = context)}")
        Button(
            onClick = {
                authViewModel.logout(navigateToChoice = navigateToChoice)
            }
        ) {
            Text(text = "logout")
        }
    }
}