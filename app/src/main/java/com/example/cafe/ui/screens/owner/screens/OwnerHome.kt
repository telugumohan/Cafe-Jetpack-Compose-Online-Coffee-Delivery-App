package com.example.cafe.ui.screens.owner.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.cafe.ui.screens.customer.Coffee
import com.example.cafe.ui.screens.customer.coffeeList
import com.example.cafe.ui.screens.owner.viewmodels.AddItemViewModel
import com.example.cafe.utils.SharedPref
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cafe.model.CoffeeModel


@Composable
fun OwnerHome(
    viewModel: AddItemViewModel = viewModel(),
    navigateToAddItems: () -> Unit
) {
    val myProducts by viewModel.products.observeAsState(listOf())
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = SharedPref.getOwnerImgUrl(context)),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                alpha = 0.8f,
                contentScale = ContentScale.Crop
            )
            Column {
                Text (
                    text = SharedPref.getOwnerCafeName(context),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Magenta
                )
                Text(
                    text = "By ${SharedPref.getOwnerName(context)}",
                    fontWeight = FontWeight.Bold,
                    color = Color.Magenta
                )
            }
        }
        Text(
            text = "Your Items",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(myProducts.isNotEmpty()) {
                items(items = myProducts) {
                    OwnerCoffeeCard(
                        coffee = it,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            } else {
                item {
                    Card(
                        modifier = Modifier
                            .padding(top = 80.dp)
                            .size(120.dp)

                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "No Items Yet!!", textAlign = TextAlign.Center)
                            IconButton(onClick = { navigateToAddItems() }) {
                                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                            }
                            Text(text = "Add One")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OwnerCoffeeCard(
    coffee: CoffeeModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(125.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = coffee.imgUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    //alpha = 0.3f,
                    contentScale = ContentScale.Crop,
                )
//                Image(
//                    painter = painterResource(id = coffee),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    //alpha = 0.3f,
//                    contentScale = ContentScale.Crop,
//                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
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
                Text(
                    text = "my price: $${coffee.currentPrice}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "original  price: $${coffee.originalPrice}",
                    style = MaterialTheme.typography.bodyLarge,
                    //fontWeight = FontWeight.Black
                )
                Text(text = "Rating: 4.3*")
                Text(
                    text = "Quantity Avail: ${coffee.quantityAvail}",
                    fontWeight = FontWeight.Black
                )
                Text(text = "Avail in stock", color = Color.Green)
            }
        }
    }
}
