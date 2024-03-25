package com.example.cafe.ui.screens.owner.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cafe.utils.SharedPref
import com.example.cafe.viewmodel.OwnerAuthViewModel

@Composable
fun OwnerMore(
    ownerAuthViewModel: OwnerAuthViewModel = viewModel(),
    navigateToChoice: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "is Customer: ${SharedPref.getIsCustomer(context = context)}")
        Button(
            onClick = {
                ownerAuthViewModel.logout(navigateToChoice)
            }
        ) {
            Text(text = "logout")
        }
    }
}