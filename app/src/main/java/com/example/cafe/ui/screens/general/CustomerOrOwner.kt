package com.example.cafe.ui.screens.general

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
import androidx.compose.ui.unit.dp

@Composable
fun CustomerOrOwner(
    navigateToCustomerLogin: () -> Unit,
    navigateToOwnerLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedButton(
            onClick = { navigateToOwnerLogin() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Owner")
        }
        Button(
            onClick = { navigateToCustomerLogin() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Customer")
        }
    }
}