package com.example.cafe.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cafe.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    @DrawableRes leadingIcon: Int = R.drawable.baseline_short_text_24,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    errorMessage: String,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    TextField (
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        maxLines = maxLines,
        label = { Text(text = labelText) },
        leadingIcon = {
            Icon(painter = painterResource(id = leadingIcon), contentDescription = null)
        },
        keyboardOptions = keyboardOptions,
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
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
