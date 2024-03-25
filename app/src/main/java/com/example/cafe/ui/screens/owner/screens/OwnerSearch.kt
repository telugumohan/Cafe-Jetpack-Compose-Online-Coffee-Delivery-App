package com.example.cafe.ui.screens.owner.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cafe.model.CoffeeModel
import com.example.cafe.ui.screens.customer.coffeeList
import com.example.cafe.ui.screens.owner.viewmodels.AddItemViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OwnerSearch(
    viewModel: AddItemViewModel = viewModel()
) {
    val myProducts by viewModel.products.observeAsState(listOf())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var searchText by rememberSaveable { mutableStateOf("") }
        OutlinedTextField (
            value = searchText,
            onValueChange = {searchText = it},
            placeholder = {Text(text = "search your dishes")},
            singleLine = true,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            trailingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null)},
            shape = MaterialTheme.shapes.large
        )
        LazyColumn (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
           if(myProducts.isNotEmpty()) {
               var filterCoffees = listOf<CoffeeModel>()
               if (searchText.isNotEmpty()) {
                   filterCoffees = myProducts.filter {
                      // it.title.contains(search, ignoreCase = true)
                       (it.title.contains(searchText, ignoreCase = true) || it.comboWith.contains(searchText, ignoreCase = true))
                   }
               } else {
                   filterCoffees = emptyList()
               }
               if (filterCoffees.isEmpty()) {
                  item {
                      Text(text = "no results found")
                  }
               } else {
                   items(items = filterCoffees) {
                       OwnerCoffeeCard(
                           coffee = it,
                           modifier = Modifier
                               .fillMaxWidth()
                       )
                   }
               }
           }
        }
    }
}