package com.example.cafe.ui.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cafe.model.BottomNavItem
import com.example.cafe.ui.screens.customer.Cart
import com.example.cafe.ui.screens.customer.CheckOutScreen
import com.example.cafe.ui.screens.customer.Favorites
import com.example.cafe.ui.screens.customer.HomeScreen
import com.example.cafe.ui.screens.customer.Orders
import com.example.cafe.ui.screens.customer.Profile
import com.example.cafe.ui.screens.customer.SearchScreen
import com.example.cafe.ui.screens.customer.SelectedCoffee
import com.example.cafe.viewmodel.CustomerFavoritesCartViewModel
import com.example.cafe.viewmodel.CustomerHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(
    navController: NavHostController,
    customerFavoritesCartViewModel : CustomerFavoritesCartViewModel = viewModel(),
    customerHomeViewModel: CustomerHomeViewModel = viewModel()
) {
    val bottomNavController = rememberNavController()
    Scaffold(
        bottomBar = { MyBottomBar(navController = bottomNavController) }
    ) {innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Routes.Home.routes,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.Home.routes) {
                HomeScreen(
                    navigateToProfile = {
                        bottomNavController.navigate(Routes.Profile.routes)
                    },
                    openDetailedCoffee = {coffeeId ->
                        val _routes = Routes.SelectedCoffee.routes.replace("{data}", coffeeId)
                        bottomNavController.navigate(_routes)

                    },
                    customerFavoritesCartViewModel = customerFavoritesCartViewModel,
                    viewModel = customerHomeViewModel
                )
            }
            composable(Routes.Search.routes) {
                SearchScreen(
                    openDetailedCoffee = {coffeeId->
                    val _routes = Routes.SelectedCoffee.routes.replace("{data}", coffeeId)
                    bottomNavController.navigate(_routes)
                    },
                    viewModel = customerHomeViewModel,
                    customerFavoritesCartViewModel = customerFavoritesCartViewModel
                )
            }
            composable(Routes.Favorites.routes) {
                Favorites(
                    openDetailedCoffee = {coffeeId->
                    val _routes = Routes.SelectedCoffee.routes.replace("{data}", coffeeId)
                    bottomNavController.navigate(_routes)
                },
                    viewModel = customerFavoritesCartViewModel,
                    customerHomeViewModel = customerHomeViewModel
                )
            }
            composable(Routes.Cart.routes) {
                Cart(
                    viewModel = customerFavoritesCartViewModel,
                    customerHomeViewModel = customerHomeViewModel,
                    navigateToCheckOut = {
                        bottomNavController.navigate(Routes.CheckOut.routes)
                    }
                )
            }
            composable(Routes.CheckOut.routes) {
                CheckOutScreen(
                    viewModel = customerHomeViewModel,
                    navigateBack = {
                        bottomNavController.navigateUp()
                    },
                    navigateToOrders = {
                        bottomNavController.navigate(Routes.Orders.routes)
                    }
                )
            }
            composable(Routes.SelectedCoffee.routes) {
                val data = it.arguments!!.getString("data")
                SelectedCoffee(
                    navigateBack = {
                                   bottomNavController.navigateUp()
                    },
                    coffeeId = data!!,
                    viewModel = customerHomeViewModel,
                    customerFavoritesCartViewModel = customerFavoritesCartViewModel,
                    navigateToCheckOut = {
                        bottomNavController.navigate(Routes.CheckOut.routes)
                    }
                )
            }
            composable(Routes.Profile.routes) {
                Profile(
                    navigateToChoice = {
                        navController.navigate(Routes.CustomerOrOwner.routes) {
                           popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Routes.Orders.routes) {
                Orders(
                    viewModel = customerFavoritesCartViewModel
                )
            }

        }
    }
}

@Composable
fun MyBottomBar(navController: NavHostController) {

    val backStackEntry = navController.currentBackStackEntryAsState()
    val list = listOf(
        BottomNavItem("Home", Routes.Home.routes, Icons.Rounded.Home),
        BottomNavItem("Search", Routes.Search.routes, Icons.Rounded.Search),
        BottomNavItem("Favorites", Routes.Favorites.routes, Icons.Rounded.FavoriteBorder),
        BottomNavItem("Orders", Routes.Orders.routes, Icons.Rounded.CheckCircle),
        BottomNavItem("Cart", Routes.Cart.routes, Icons.Rounded.ShoppingCart),
    )

    BottomAppBar {
        list.forEach {
            val selected: Boolean = it.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(imageVector = it.icon, contentDescription = it.title)
                }
            )
        }
    }
}