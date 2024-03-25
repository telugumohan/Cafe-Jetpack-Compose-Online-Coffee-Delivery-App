package com.example.cafe.ui.screens.customer

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cafe.model.CoffeeModel
import com.example.cafe.ui.screens.owner.screens.OwnerCoffeeCard
import com.example.cafe.viewmodel.CustomerFavoritesCartViewModel
import com.example.cafe.viewmodel.CustomerHomeViewModel

@Composable
fun SearchScreen(
    viewModel: CustomerHomeViewModel,
    openDetailedCoffee: (String) -> Unit,
    customerFavoritesCartViewModel: CustomerFavoritesCartViewModel
) {
    val allProducts by viewModel.allProducts.observeAsState(listOf())
    val favorites by customerFavoritesCartViewModel.favorites.observeAsState(listOf())
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
            trailingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
            shape = MaterialTheme.shapes.large
        )
        LazyColumn (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if(allProducts.isNotEmpty()) {
                var filterCoffees = listOf<CoffeeModel>()
                if (searchText.isNotEmpty()) {
                    filterCoffees = allProducts.filter {
                        // it.title.contains(search, ignoreCase = true)
                        (it.title.contains(searchText, ignoreCase = true) || it.comboWith.contains(searchText, ignoreCase = true))
                    }
                } else {
                    filterCoffees = emptyList()
                }
                if (filterCoffees.isNotEmpty()) {
                    items(items = filterCoffees) {
                        CoffeeCard(
                            coffee = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable { openDetailedCoffee(it.coffeeId) },
                            isFavorite = (favorites.contains(it)),
                            removeOrAddFromFav = {

                            }
                        )
                    }
                } else if(filterCoffees.isEmpty() && searchText.isNotEmpty()) {
                    item {
                        Text(text = "no results found", color = Color.Red)
                    }
                }
            }
        }
    }
}