package com.example.cafe.ui.screens.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.cafe.viewmodel.CustomerFavoritesCartViewModel
import com.example.cafe.viewmodel.MyOrderItem

@Composable
fun Orders(
    viewModel: CustomerFavoritesCartViewModel
) {
    val myOrderItems by viewModel.myOrders.observeAsState(listOf())
   Scaffold(
       topBar = {
           Text(text = "My Orders")
       }
   ) {paddingValues ->
       LazyColumn(contentPadding = paddingValues) {
           myOrderItems?.forEach {
               item {
                   SingleOrderItem(myOrderItem = it)
               }
           }
       }
   }
}


@Composable
fun SingleOrderItem(
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
                        Text(text = "Quantity: $${myOrderItem.qty}", color = Color.Black)
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
                        val currentPrice: Double = myOrderItem.coffeeModel.currentPrice.toDoubleOrNull() ?: 0.0
                        val showPrice: Double = currentPrice * myOrderItem.qty
                        Text(
                            text = "Order Value: $$showPrice",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                }
                Text(
                    text = "Delivered By Thu Feb 8 | $62",
                    color = Color.Gray
                )
                Text(text = "Order Status: ${myOrderItem.orderStatus}")
                Text(text = "Order Id: #${myOrderItem.myOrderItemId}")

            }
        }
        Divider(thickness = 5.dp, color = Color.LightGray, modifier = Modifier.fillMaxWidth())
    }
}