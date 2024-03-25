package com.example.cafe.ui.navigation

sealed class Routes(val routes: String) {

    // Main Nav
    object Login: Routes("login")
    object Register: Routes("register")
    object Splash: Routes("splash")
    object CustomerOrOwner: Routes("customer_or_owner")
    object BottomNav: Routes("bottom_nav")
    object OwnerBottomNav: Routes("owner_bottom_nav")


    // Customer
    object Home: Routes("home")
    object Search: Routes("search")
    object Favorites: Routes("favorites")
    object Cart: Routes("cart")
    object SelectedCoffee: Routes("selected_coffee/{data}")
    object Profile: Routes("profile")
    object Orders: Routes("orders")
    object CheckOut: Routes("checkout")


    // Owner
    object OwnerHome: Routes("owner_home")
    object OwnerSearch: Routes("owner_search")
    object OwnerAddItems: Routes("owner_add_items")
    object OwnerOrders: Routes("owner_orders")
    object OwnerMore: Routes("owner_more")
    object OwnerRegister: Routes("owner_register")
    object OwnerLogin: Routes("owner_login")
}
