package com.example.cafe.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.cafe.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField (
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    errorMessage: String
) {
    var showPassword by remember{ mutableStateOf(false) }
    val trailingIcon = if (showPassword) {
        R.drawable.baseline_visibility_off_24
    } else {
        R.drawable.baseline_visibility_24
    }
    TextField (
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        maxLines = 1,
        label = { Text(text = labelText) },
        leadingIcon = {
            Icon(painter = painterResource(id = leadingIcon), contentDescription = null)
        },
        keyboardOptions = keyboardOptions,
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(painter = painterResource(id = trailingIcon), contentDescription = null)
            }
        },
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )
    if (errorMessage.isNotEmpty()) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

