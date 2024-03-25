package com.example.cafe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cafe.ui.screens.general.CustomerOrOwner
import com.example.cafe.ui.screens.customer.Cart
import com.example.cafe.ui.screens.customer.CheckOutScreen
import com.example.cafe.ui.screens.customer.Favorites
import com.example.cafe.ui.screens.customer.HomeScreen
import com.example.cafe.ui.screens.customer.Login
import com.example.cafe.ui.screens.customer.Orders
import com.example.cafe.ui.screens.customer.Profile
import com.example.cafe.ui.screens.customer.Register
import com.example.cafe.ui.screens.customer.SearchScreen
import com.example.cafe.ui.screens.customer.SelectedCoffee
import com.example.cafe.ui.screens.general.Splash
import com.example.cafe.ui.screens.owner.screens.OwnerAddItems
import com.example.cafe.ui.screens.owner.screens.OwnerHome
import com.example.cafe.ui.screens.owner.OwnerLogin
import com.example.cafe.ui.screens.owner.screens.OwnerMore
import com.example.cafe.ui.screens.owner.screens.OwnerOrders
import com.example.cafe.ui.screens.owner.OwnerRegister
import com.example.cafe.ui.screens.owner.screens.OwnerSearch
import com.example.cafe.viewmodel.CustomerFavoritesCartViewModel
import com.example.cafe.viewmodel.CustomerHomeViewModel

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    customerFavoritesCartViewModel : CustomerFavoritesCartViewModel = viewModel(),
    customerHomeViewModel: CustomerHomeViewModel = viewModel()
) {
    NavHost(navController = navController, startDestination = Routes.Splash.routes) {

        composable(Routes.Splash.routes) {
            Splash(
                navigateToChoice = {
                    navController.navigate(Routes.CustomerOrOwner.routes) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                        //navController.popBackStack(Routes.Splash.routes, inclusive = true)
                    }
                },
                isCustomer = {
                    if (it) {
                        navController.navigate(Routes.BottomNav.routes) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(Routes.OwnerBottomNav.routes) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(Routes.CustomerOrOwner.routes) {
            CustomerOrOwner(
                navigateToCustomerLogin = {
                    navController.navigate(Routes.Login.routes) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                navigateToOwnerLogin = {
                    navController.navigate(Routes.OwnerLogin.routes) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }

        // Customer
        composable(Routes.Login.routes) {
            Login(
                navigateToRegister = {
                                     navController.navigate(Routes.Register.routes) {
                                         popUpTo(navController.graph.startDestinationId)
                                         launchSingleTop = true
                                     }
                },
                navigateToBottomNav = {
                    navController.navigate(Routes.BottomNav.routes) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.Register.routes) {
            Register(
                navigateToLogin = {
                    navController.navigate(Routes.Login.routes) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                navigateToBottomNav = {
                    navController.navigate(Routes.BottomNav.routes) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.BottomNav.routes) {
            BottomNav(navController = navController)
        }
                   // common routes
        composable(Routes.Home.routes) {
            HomeScreen(
                navigateToProfile = {},
                openDetailedCoffee = {},
                customerFavoritesCartViewModel = customerFavoritesCartViewModel,
                viewModel = customerHomeViewModel
            )
        }
        composable(Routes.Search.routes) {
            SearchScreen(
                openDetailedCoffee = {},
                customerFavoritesCartViewModel = customerFavoritesCartViewModel,
                viewModel = customerHomeViewModel
            )
        }
        composable(Routes.Favorites.routes) {
            Favorites(
                openDetailedCoffee = {},
                customerHomeViewModel = customerHomeViewModel,
                viewModel = customerFavoritesCartViewModel
            )
        }
        composable(Routes.Cart.routes) {
            Cart(
                navigateToCheckOut = {},
                viewModel = customerFavoritesCartViewModel,
                customerHomeViewModel = customerHomeViewModel
            )
        }
        composable(Routes.CheckOut.routes) {
            CheckOutScreen(
                viewModel = customerHomeViewModel,
                navigateBack = {},
                navigateToOrders = {}
            )
        }
        composable(Routes.SelectedCoffee.routes) {
            SelectedCoffee(
                navigateBack = {},
                coffeeId = "",
                viewModel = customerHomeViewModel,
                customerFavoritesCartViewModel = customerFavoritesCartViewModel,
                navigateToCheckOut = {}
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
            Orders(viewModel = customerFavoritesCartViewModel)
        }



        // Owner
        composable(Routes.OwnerBottomNav.routes) {
            OwnerBottomNav(navController = navController)
        }

        composable(Routes.OwnerLogin.routes) {
           OwnerLogin(
               navigateToOwnerRegister = {
                   navController.navigate(Routes.OwnerRegister.routes) {
                       popUpTo(navController.graph.startDestinationId)
                       launchSingleTop = true
                   }
               },
               navigateToOwnerBottomNav = {
                   navController.navigate(Routes.OwnerBottomNav.routes) {
                       popUpTo(navController.graph.startDestinationId)
                       launchSingleTop = true
                   }
               }
           )
        }
        composable(Routes.OwnerRegister.routes) {
           OwnerRegister(
               navigateToOwnerLogin = {
                   navController.navigate(Routes.OwnerLogin.routes) {
                       popUpTo(navController.graph.startDestinationId)
                       launchSingleTop = true
                   }
               },
               navigateToOwnerBottomNav = {
                   navController.navigate(Routes.OwnerBottomNav.routes) {
                       popUpTo(navController.graph.startDestinationId)
                       launchSingleTop = true
                   }
               }
           )
        }
        composable(Routes.OwnerHome.routes) {
            OwnerHome(
                navigateToAddItems = {}
            )
        }
        composable(Routes.OwnerSearch.routes) {
            OwnerSearch()
        }
        composable(Routes.OwnerAddItems.routes) {
            OwnerAddItems(navigateToHome = {})
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