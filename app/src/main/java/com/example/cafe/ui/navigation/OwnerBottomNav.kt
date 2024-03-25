package com.example.cafe.ui.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cafe.model.BottomNavItem
import com.example.cafe.ui.screens.owner.screens.OwnerAddItems
import com.example.cafe.ui.screens.owner.screens.OwnerHome
import com.example.cafe.ui.screens.owner.screens.OwnerMore
import com.example.cafe.ui.screens.owner.screens.OwnerOrders
import com.example.cafe.ui.screens.owner.screens.OwnerSearch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerBottomNav(
    navController: NavHostController
) {
    val ownerBottomNavController: NavHostController = rememberNavController()
    //val bottomNavController = rememberNavController()
    Scaffold(
        bottomBar = { OwnerBottomBar(navController = ownerBottomNavController) }
    ) {innerPadding ->
        NavHost(
            navController = ownerBottomNavController,
            startDestination = Routes.OwnerHome.routes,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.OwnerHome.routes) {
                OwnerHome(
                    navigateToAddItems = {
                        ownerBottomNavController.navigate(Routes.OwnerAddItems.routes){
                            popUpTo(ownerBottomNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Routes.OwnerSearch.routes) {
                OwnerSearch()
            }
            composable(Routes.OwnerAddItems.routes) {
                OwnerAddItems(
                    navigateToHome = {
                        ownerBottomNavController.navigate(Routes.OwnerHome.routes) {
                            popUpTo(ownerBottomNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Routes.OwnerOrders.routes) {
                OwnerOrders()
            }
            composable(Routes.OwnerMore.routes) {
                OwnerMore(
                    navigateToChoice = {
                        navController.navigate(Routes.CustomerOrOwner.routes) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }

        }
    }
}


@Composable
fun OwnerBottomBar(navController: NavHostController) {

    val backStackEntry = navController.currentBackStackEntryAsState()
    val list = listOf(
        BottomNavItem("OwnerHome", Routes.OwnerHome.routes, Icons.Rounded.Home),
        BottomNavItem("OwnerSearch", Routes.OwnerSearch.routes, Icons.Rounded.Search),
        BottomNavItem("OwnerAddItem", Routes.OwnerAddItems.routes, Icons.Rounded.Add),
        BottomNavItem("OwnerOrders", Routes.OwnerOrders.routes, Icons.Rounded.CheckCircle),
        BottomNavItem("OwnerMore", Routes.OwnerMore.routes, Icons.Rounded.Menu),
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