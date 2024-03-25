package com.example.cafe.ui.screens.customer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.cafe.R
import com.example.cafe.viewmodel.CustomerFavoritesCartViewModel
import com.example.cafe.viewmodel.CustomerHomeViewModel
import com.example.cafe.viewmodel.MyOrderItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckOutScreen(
    viewModel: CustomerHomeViewModel,
    navigateBack: () -> Unit,
    navigateToOrders: () -> Unit
) {

    val checkList by viewModel.checkOutState.collectAsState()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Check Out") },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                              viewModel.addToOrders(myOrderItemList = checkList.myOrderItems, context = context)
                        navigateToOrders()
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(text = "Confirm Order")
                }
            }
        }
    ) {paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val originalPrice = mutableStateOf(0.0)
            val currentPrice = mutableStateOf(0.0)
           checkList.myOrderItems.forEach {
              item {
                  SingleCheckItem(myOrderItem = it, modifier = Modifier.fillMaxWidth())
              }
               originalPrice.value += it.qty * it.coffeeModel.originalPrice.toDouble()
               currentPrice.value += it.qty * it.coffeeModel.currentPrice.toDouble()
           }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //verticalArrangement = Arrangement.spacedBy()
                ) {
                    Text(
                        text = "Price Details",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Price (${checkList.myOrderItems.size} item/s)")
                            Text(text = "$${originalPrice.value}")
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Discount")
                            Text(text = "-$${originalPrice.value - currentPrice.value}", color = Color.Green)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Delivery Charges")
                            Text(text = "$100")
                        }
                    }
                    Divider(thickness = 1.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Total Amount", fontWeight = FontWeight.Bold)
                        Text(text = "$${currentPrice.value+100.0}", fontWeight = FontWeight.Bold)
                    }
                    Divider(thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun SingleCheckItem(
    modifier: Modifier = Modifier,
    myOrderItem: MyOrderItem
) {
    Column (
        modifier = modifier
    ){
        Divider(thickness = 5.dp, color = Color.LightGray, modifier = Modifier.fillMaxWidth())
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.2f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter =  rememberAsyncImagePainter(model = myOrderItem.coffeeModel.imgUrl) ,          //painterResource(id = coffee.imgResId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(96.dp)
                                .clip(shape = MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop
                        )
                        Text(text = "Quantity: $${myOrderItem.qty}", color = Color.Red)
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = myOrderItem.coffeeModel.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black
                            )
                            Text(text = "with ${myOrderItem.coffeeModel.comboWith}")
                        }
                        Text(
                            text = "Rating: 4.2*",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        val currentPrice: Double = myOrderItem.coffeeModel.currentPrice.toDoubleOrNull() ?: 0.0
                        val showPrice: Double = currentPrice * myOrderItem.qty
                        Text(
                            text = "14% Off | $$showPrice",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                }
                Text(
                    text = "Delivered By Thu Feb 8 | $62",
                    color = Color.Gray
                )
            }
        }
        Divider(thickness = 5.dp, color = Color.LightGray, modifier = Modifier.fillMaxWidth())
    }
}