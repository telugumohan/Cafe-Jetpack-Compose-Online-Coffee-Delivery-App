package com.example.cafe.ui.screens.owner.screens

import  androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.cafe.R
import com.example.cafe.ui.screens.customer.Coffee
import com.example.cafe.ui.screens.customer.coffeeList
import com.example.cafe.ui.screens.owner.viewmodels.AddItemViewModel
import com.example.cafe.viewmodel.OwnerOrderItem


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun OwnerOrders(
    viewModel: AddItemViewModel = viewModel()
) {
    val myOrders by viewModel.ownerOrders.observeAsState(listOf())
    Scaffold(
        topBar = {
            OwnerCenterAlignedTopAppBar(title = "Your Orders", canNavigateBack = false, navigateBack = {})
        }
    ) {paddingValues ->  
        LazyColumn(
            contentPadding = paddingValues,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            if (myOrders.isNotEmpty()) {
                items(items = myOrders) {
                    OwnerOrderCard(order = it, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}


@Composable
fun OwnerOrderCard(
    order: OwnerOrderItem,
    modifier: Modifier = Modifier
) {
    var showCustomerDetails by remember { mutableStateOf(false) }
    var showOrderStatus by remember { mutableStateOf(false) }
    Box {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                            painter = rememberAsyncImagePainter(model = order.coffeeModel.imgUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize(),
                            //alpha = 0.3f,
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = order.coffeeModel.title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "with ${order.coffeeModel.comboWith}",
                            color = Color.Gray
                        )
                        Text(
                            text = "price: $${order.coffeeModel.currentPrice}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Customer Details",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = { showCustomerDetails = !showCustomerDetails },
                            ) {
                                Icon(
                                    painter = painterResource(id = if (showCustomerDetails) R.drawable.baseline_expand_less_24 else R.drawable.baseline_expand_more_24),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    if (showCustomerDetails) {
                        Text(
                            text = "Name: ${order.customerName}",
                        )
                        Text(
                            text = "Quantity Ordered: ${order.qty} pcs",
                        )
                        val price = order.coffeeModel.currentPrice.toDouble() * order.qty
                        Text(text = "Amount to pay: $${price}")
                        Text(
                            text = "Delivery Location: ${order.deliveryLocation}",
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Order Status",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = { showOrderStatus = !showOrderStatus },
                            ) {
                                Icon(
                                    painter = painterResource(id = if (showOrderStatus) R.drawable.baseline_expand_less_24 else R.drawable.baseline_expand_more_24),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    if (showOrderStatus) {
                        Text(
                            text = "Mode of payment: UPI",
                        )
                        Text(
                            text = "Is payment done? : YES",
                        )
                        Text(text = "Pick up date: 01 FEB 2024")
                        Text(
                            text = "Expected delivery date: 04 FEB 2024",
                        )
                        Text(text = "Delivery Status: SHIPPING")
                    }

                }
            }
        }
        Icon(
            painter = painterResource(id =  R.drawable.baseline_pending_24),//R.drawable.baseline_done_outline_24),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 8.dp, end = 16.dp)
                .align(alignment = Alignment.TopEnd),
            tint = Color(0xFFEC9464)
        )

    }
}
