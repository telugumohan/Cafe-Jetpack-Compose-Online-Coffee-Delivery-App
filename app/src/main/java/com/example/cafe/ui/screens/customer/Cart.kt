package com.example.cafe.ui.screens.customer

import androidx.lifecycle.viewmodel.compose.viewModel
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.cafe.R
import com.example.cafe.model.CoffeeModel
import com.example.cafe.viewmodel.CustomerFavoritesCartViewModel
import com.example.cafe.viewmodel.CustomerHomeViewModel
import com.example.cafe.viewmodel.MyOrderItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cart(
    viewModel: CustomerFavoritesCartViewModel,
    customerHomeViewModel: CustomerHomeViewModel,
    navigateToCheckOut: () -> Unit
) {
    val cartList by viewModel.cart.observeAsState(listOf())
    //val cartIds by viewModel.cartIds.observeAsState(listOf())

    val savedForLaterList by viewModel.saveForLater.observeAsState(listOf())
    val savedForLaterIds by viewModel.saveForLaterIds.observeAsState(listOf())

    Scaffold(
        topBar = {
           CenterAlignedTopAppBar(title = { Text(text = "My Cart") })
        }
    ) {paddingValues ->
        if (cartList.isNotEmpty() || savedForLaterList.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
//                val originalPrice by viewModel.totalOriginalPrice
//                val currentPrice by viewModel.totalCurrentPrice
                val originalPrice = mutableStateOf(0.0)
                val currentPrice = mutableStateOf(0.0)
                val myCurrentOrderItemList = mutableStateOf<List<MyOrderItem>>(listOf())
                if (cartList.isNotEmpty()) {
                    cartList.forEach {coffeeModel ->
                        var qty by remember { mutableStateOf(1) }
                        originalPrice.value += qty * coffeeModel.originalPrice.toDouble()
                        currentPrice.value += qty * coffeeModel.currentPrice.toDouble()
                        // Create a MyOrderItem object for the current coffeeModel and add it to myCurrentOrderItemList
                        val myOrderItem = MyOrderItem(coffeeModel = coffeeModel, qty = qty)
                        myCurrentOrderItemList.value += myOrderItem

                        SingleCartItem(
                            coffeeModel = coffeeModel,
                            modifier = Modifier.fillMaxWidth(),
                            removeFromCart = {
                                if(cartList.contains(coffeeModel)) {
                                    customerHomeViewModel.removeFromCart(coffeeModel)
                                }
                            },
                            onQtyChange = {qty = it},
                            currentQty = qty,
                            saveForLater = {
                                if (!savedForLaterIds.contains(coffeeModel.coffeeId)) {
                                    customerHomeViewModel.addToSaveForLater(coffeeModel)
                                }
                            },
                            isSaved = false,
                            customerHomeViewModel = customerHomeViewModel,
                            navigateToCheckOut = navigateToCheckOut
                        )
                    }
                    myCurrentOrderItemList.value = myCurrentOrderItemList.value.distinct()
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
                                Text(text = "Price (${cartList.size} item/s)")
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
                        Button(
                            onClick = {
                                customerHomeViewModel.updateCheckOutState(myOrderItemList = myCurrentOrderItemList.value)
                                navigateToCheckOut()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Check Out")
                        }
                    }
                }
                if (savedForLaterList.isNotEmpty()) {
                    Text(text = "Saved For Later")
                    savedForLaterList.forEach { coffeeModel ->
                        var qty by remember { mutableStateOf(1) }
                        SingleCartItem(
                            coffeeModel = coffeeModel,
                            removeFromCart = {
                                if(cartList.contains(coffeeModel)) {
                                    customerHomeViewModel.removeFromCart(coffeeModel)
                                }
                            },
                            onQtyChange = {qty = it},
                            currentQty = qty,
                            saveForLater = {  },
                            isSaved = true,
                            removeFromSaved = {
                                if (savedForLaterIds.contains(coffeeModel.coffeeId)) {
                                    customerHomeViewModel.removeToSaveForLater(coffeeModel)
                                }
                            },
                            navigateToCheckOut = navigateToCheckOut,
                            customerHomeViewModel = customerHomeViewModel
                        )
                    }
                }

            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.size(120.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "No Items in the Cart!!")
                    }
                }
            }
        }
    }
}

@Composable
fun SingleCartItem(
    coffeeModel: CoffeeModel,
    modifier: Modifier = Modifier,
    removeFromCart: () -> Unit,
    onQtyChange: (Int) -> Unit,
    currentQty: Int,
    saveForLater: () -> Unit,
    isSaved: Boolean,
    removeFromSaved: () -> Unit = {},
    navigateToCheckOut: () -> Unit,
    customerHomeViewModel: CustomerHomeViewModel
) {
    var showQtyMenu by remember { mutableStateOf(false) }
    val myOrderItem = MyOrderItem(coffeeModel = coffeeModel, qty = currentQty)

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
                            painter =  rememberAsyncImagePainter(model = coffeeModel.imgUrl) ,          //painterResource(id = coffee.imgResId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(96.dp)
                                .clip(shape = MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop
                        )
                        Card(
                            modifier = Modifier
                                .size(height = 24.dp, width = 96.dp),
                            colors = CardDefaults.cardColors(Color.White),
                            shape = RoundedCornerShape(0.dp),
                            border = BorderStroke(width = 1.dp, color = Color.Gray)
                        ) {
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Qty: $currentQty")
                                IconButton(onClick = { showQtyMenu = !showQtyMenu }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_expand_more_24),
                                        contentDescription = null
                                    )
                                }
                                DropdownMenu(
                                    expanded = showQtyMenu,
                                    onDismissRequest = { showQtyMenu = !showQtyMenu }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(text = "1") },
                                        onClick = {
                                           onQtyChange(1)
                                            showQtyMenu = !showQtyMenu
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(text = "2") },
                                        onClick = {
                                            onQtyChange(2)
                                            showQtyMenu = !showQtyMenu
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(text = "5") },
                                        onClick = {
                                            onQtyChange(5)
                                            showQtyMenu = !showQtyMenu
                                        }
                                    )
                                }
                            }
                        }
                        //Text(text = "only 3 left", color = Color.Red)
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
                                text = coffeeModel.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black
                            )
                            Text(text = "with ${coffeeModel.comboWith}")
                        }
                        Text(
                            text = "Rating: 4.2*",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        val currentPrice: Double = coffeeModel.currentPrice.toDoubleOrNull() ?: 0.0
                        val showPrice: Double = currentPrice * currentQty
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { removeFromCart() },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        Text(text = "Remove", textAlign = TextAlign.Center)
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                if (isSaved) {
                                    removeFromSaved()
                                } else {
                                    saveForLater()
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(painter = painterResource(id = if (isSaved) R.drawable.baseline_upload_24 else R.drawable.baseline_save_alt_24) , contentDescription = null)
                        Text(text = if (isSaved) "Add to CurrentList" else "Save for later", textAlign = TextAlign.Center)
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                customerHomeViewModel.updateCheckOutState(myOrderItemList = listOf(myOrderItem))
                                navigateToCheckOut()
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                        Text(text = "Buy this now", textAlign = TextAlign.Center)
                    }
                }
            }
        }
        Divider(thickness = 5.dp, color = Color.LightGray, modifier = Modifier.fillMaxWidth())
    }

}

