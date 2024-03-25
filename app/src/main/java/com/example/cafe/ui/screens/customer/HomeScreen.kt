package com.example.cafe.ui.screens.customer

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.cafe.R
import com.example.cafe.ui.theme.CafeTheme
import com.example.cafe.ui.theme.Shapes
import com.example.cafe.utils.SharedPref
import com.example.cafe.viewmodel.CustomerHomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cafe.model.CoffeeModel
import com.example.cafe.viewmodel.CustomerFavoritesCartViewModel


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomePreview() {
    CafeTheme {
        //HomeScreen(navigateToProfile = {})
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    customerFavoritesCartViewModel: CustomerFavoritesCartViewModel,
    navigateToProfile: () -> Unit,
    viewModel: CustomerHomeViewModel,
    openDetailedCoffee: (String) -> Unit
) {
    val allProducts by viewModel.allProducts.observeAsState(listOf())
    val favorites by customerFavoritesCartViewModel.favorites.observeAsState(listOf())
    Scaffold(
        topBar = {
            MyTopAppBar(navigateToProfile)
        }
    ) {it ->
        Box() {
            Image(
                painter = painterResource(id = R.drawable.x),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            HomeBody(modifier = Modifier
                .fillMaxSize()
                .padding(it),
                allProducts = allProducts,
                openDetailedCoffee = openDetailedCoffee,
                favorites = favorites,
                removeOrAddFromFav = {coffeeModel ->
                   if (favorites.contains(coffeeModel)) {
                       viewModel.removeFromFavorites(coffeeModel)
                   } else {
                       viewModel.addToFavorites(coffeeModel)
                   }
                }
            )
        }
    }
}


@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    allProducts: List<CoffeeModel>,
    openDetailedCoffee: (String) -> Unit,
    favorites: List<CoffeeModel>,
    removeOrAddFromFav: (CoffeeModel) -> Unit
) {
    var topSearchBarText by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextFielder(
            value = topSearchBarText,
            onValueChange = {topSearchBarText = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )

        OffersCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        FilterRow(modifier = Modifier.fillMaxWidth())
        LazyGridItems(
            modifier = Modifier.fillMaxSize(),
            allProducts = allProducts,
            openDetailedCoffee = openDetailedCoffee,
            favorites = favorites,
            removeOrAddFromFav = removeOrAddFromFav
        )
    }
}

@Composable
fun LazyGridItems (
    modifier: Modifier = Modifier,
    allProducts: List<CoffeeModel>,
    openDetailedCoffee: (String) -> Unit,
    favorites: List<CoffeeModel>,
    removeOrAddFromFav: (CoffeeModel) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
    ) {
        if(allProducts.isNotEmpty()) {
            items(items = allProducts) {
                CoffeeCard(
                    coffee = it,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { openDetailedCoffee(it.coffeeId) },
                    isFavorite = (favorites.contains(it)),
                    removeOrAddFromFav = {
                        removeOrAddFromFav(it)
                    }
                )
            }
        } else {
            item {
                Text(text = "Empty Bro!!")
            }
        }
    }
}

@Composable
fun CoffeeCard(
    coffee: CoffeeModel,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    removeOrAddFromFav: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = coffee.imgUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Text(
                    text = coffee.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "with ${coffee.comboWith}",
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$${coffee.currentPrice}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Black
                    )
                    Card(
                        shape = Shapes.medium,
                        modifier = Modifier
                            .clickable { removeOrAddFromFav() }
                    ) {
                        Icon (
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier
                                .background(Color(185, 122, 87))
                                .padding(4.dp),
                            tint = if (isFavorite) Color.Red else Color.White
                        )
                    }
                }
            }

        }
    }
}

val coffeeList = listOf(
    Coffee(R.drawable.cf1, "Cappuccino", "Chocolate", 4.5, 4.8),
    Coffee(R.drawable.cf2, "Cappuccino", "Oat milk", 4.1, 3.1),
    Coffee(R.drawable.cf3, "Dum Chai", "Oat milk", 2.5, 4.1),
    Coffee(R.drawable.cf4, "Machiato", "Chocolate", 4.5, 3.8),
    Coffee(R.drawable.cf5, "Cappuccino", "Chocolate", 3.5, 2.9),
    Coffee(R.drawable.cf6, "Cappuccino", "Oat milk", 4.9, 4.2),
    Coffee(R.drawable.cf7, "Dum Chai", "Chocolate", 5.1, 4.0),
    Coffee(R.drawable.cf8, "Machiato", "Oat milk", 4.5, 4.8),
)

data class Coffee (
    @DrawableRes val imgResId: Int,
    val title: String = "",
    val comboWith: String = "",
    val price: Double = 0.0,
    val rating: Double = 0.0
)

@Composable
fun FilterRow(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = filterItems) { currItem ->
            FilterCard(
                text = currItem
            )
        }
    }
}

@Composable
fun FilterCard(
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
        )
    }
}

val filterItems = listOf(
    "Cappuccino",
    "Machiato",
    "Latte",
    "Kubmokonam Degree Coffee",
    "Dum Chai"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFielder(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = Color.White)
        },
        trailingIcon = {
            Icon(painter = painterResource(id = R.drawable.tune_24px), contentDescription = null, tint = Color.White)
        },
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(containerColor = Color(185, 122, 87)),
//        TextFieldDefaults.colors(
//            focusedContainerColor = Color(185, 122, 87),
//            unfocusedContainerColor = Color(185, 122, 87),
////            focusedTextColor = Color.White,
////            unfocusedTextColor = Color.White
//        ),
        shape = Shapes.large,
        keyboardOptions = keyboardOptions,
        singleLine = true,

        )
}

@Composable
fun OffersCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.x1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navigateToProfile: () -> Unit) {
    TopAppBar(
        title = {
            CurrentLocation()
        },
        actions = {
            ProfileCard(navigateToProfile)
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color(185, 122, 87))
        //TopAppBarDefaults.topAppBarColors(containerColor = Color(185, 122, 87))
    )
}

@Composable
fun CurrentLocation() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
        ) {
            Text(
                text = "Location",
                color = Color.DarkGray,
                style = MaterialTheme.typography.titleSmall
            )
            val loc = "Kurnool, Andhra Pradesh,  Pradesh Kurnool, Andhra Pradesh Kurnool, Andhra Pradesh"
            val curLoc =
                if(loc.length > 25) {
                    loc.substring(0, 25)+ " ..."
                } else {
                    loc
                }
            Text(text = curLoc, style = MaterialTheme.typography.titleMedium)
        }
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_expand_more_24),
                contentDescription = "show more",
                tint = Color.White
            )
        }
    }
}

@Composable
fun ProfileCard(navigateToProfile: () -> Unit) {
    Card(
        modifier = Modifier
            .size(40.dp)
            .clip(shape = Shapes.medium)
            .clickable { navigateToProfile() }
    ) {
        val context = LocalContext.current
        Image(
            painter = rememberAsyncImagePainter(model = SharedPref.getImgUrl(context)),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}