package com.example.cafe.ui.screens.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cafe.R
import com.example.cafe.ui.theme.CafeTheme
import com.example.cafe.ui.theme.Shapes
import com.example.cafe.viewmodel.CustomerHomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.cafe.model.CoffeeModel
import com.example.cafe.viewmodel.CustomerFavoritesCartViewModel
import com.example.cafe.viewmodel.MyOrderItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedCoffee(
    navigateBack: () -> Unit,
    coffeeId: String,
    viewModel: CustomerHomeViewModel,
    customerFavoritesCartViewModel: CustomerFavoritesCartViewModel,
    navigateToCheckOut: () -> Unit
) {


    viewModel.fetchCoffeeModel(coffeeId)
   val cart by customerFavoritesCartViewModel.cart.observeAsState(listOf())
    val myCoffeeModelById by viewModel.coffeeModelById.observeAsState(null)
    val favorites by customerFavoritesCartViewModel.favorites.observeAsState(listOf())


    Scaffold(
        topBar = {
          if (myCoffeeModelById != null) {
              MyCenterTopAppBar(
                  "Details",
                  navigateBack = navigateBack,
                  addToFavorite = {
                      if (favorites.contains(myCoffeeModelById)) {
                          viewModel.removeFromFavorites(myCoffeeModelById!!)
                      } else {
                          viewModel.addToFavorites(myCoffeeModelById!!)
                      }
                  },
                  isFavorite = (favorites.contains(myCoffeeModelById))
              )
          }
        }
    ) {it ->
        if (myCoffeeModelById != null) {
            val myOrderItem = MyOrderItem(coffeeModel = myCoffeeModelById!!, qty = 1)
            DetailsBody (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                coffeeModel = myCoffeeModelById!!,
                addToCart = {
                    if (cart.contains(myCoffeeModelById)) {
                        viewModel.removeFromCart(myCoffeeModelById!!)
                    } else {
                        viewModel.addToCart(myCoffeeModelById!!)
                    }
                },
                isInCart = cart.contains(myCoffeeModelById),
                buyNow = {
                    viewModel.updateCheckOutState(myOrderItemList = listOf(myOrderItem))
                    navigateToCheckOut()
                }
            )
        }
    }
}

@Composable
fun DetailsBody(
    modifier: Modifier = Modifier,
    coffeeModel: CoffeeModel,
    addToCart: () -> Unit,
    isInCart: Boolean,
    buyNow: () -> Unit
) {
    if (coffeeModel != null) {
        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = coffeeModel.imgUrl),          //painterResource(id = R.drawable.cf1),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
           Column {
               Text(text = coffeeModel.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
               Text(
                   text = "with ${coffeeModel.comboWith}",
                   color = Color.Gray
               )
           }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_star_rate_24),
                    contentDescription = null,
                    tint = Color(185, 122, 87)
                )
                Text(text = "4.3", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
                Text(text = "(210)", style = MaterialTheme.typography.headlineSmall)
            }
            Divider(thickness = 1.dp, modifier = Modifier
                .fillMaxWidth()
            )
            Text(text = "Description", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
            val description = coffeeModel.description
            val displayedDescription =
                if (description.length > 250) {
                    description.substring(0, 250) + "...Read More"
                } else {
                    description
                }
            Text(
                text = displayedDescription, textAlign = TextAlign.Justify
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Price", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
                Text(text = "$${coffeeModel.currentPrice}", color = Color(185, 122, 87))
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { addToCart() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = Shapes.medium
                ) {
                    Text(text = if (isInCart) "Remove From Cart" else "Add To Cart")
                }
                Button(
                    onClick = {
                       buyNow()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(185, 122, 87)),
                    shape = Shapes.medium,
                    modifier = Modifier.fillMaxWidth()  //size(200.dp, 64.dp)
                ) {
                    Text(text = "Buy Now")
                }
            }
        }
    } else {
        Text(text = "Not Found Bro...")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCenterTopAppBar(
    title: String,
    navigateBack: () -> Unit,
    addToFavorite: () -> Unit,
    isFavorite: Boolean = false
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon (
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = { addToFavorite() }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isFavorite) Color.Red else Color.Black
                )
            }
        }
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DetailPreview() {
    CafeTheme {
       // SelectedCoffee()
    }
}