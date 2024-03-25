package com.example.cafe.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cafe.model.CoffeeModel
import com.example.cafe.ui.screens.owner.viewmodels.OwnerData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class CustomerFavoritesCartViewModel : ViewModel() {

//    val totalOriginalPrice = mutableStateOf(0.0)
//    val totalCurrentPrice = mutableStateOf(0.0)


    private val fireStoreDb = Firebase.firestore
    private var customerRef: DocumentReference? = null

    private val _saveForLater = MutableLiveData<List<CoffeeModel>>()
    val saveForLater: LiveData<List<CoffeeModel>>
        get() = _saveForLater

    private val _saveForLaterIds = MutableLiveData<List<String>>()
    val saveForLaterIds: LiveData<List<String>>
        get() = _saveForLaterIds


    private val _myOrders = MutableLiveData<List<MyOrderItem>?>()
    val myOrders: MutableLiveData<List<MyOrderItem>?>
        get() = _myOrders

    private val _myOrdersIds = MutableLiveData<List<String>>()
    val myOrdersIds: LiveData<List<String>>
        get() = _myOrdersIds

    private val _favorites = MutableLiveData<List<CoffeeModel>>()
    val favorites: LiveData<List<CoffeeModel>>
        get() = _favorites

    private val _cart = MutableLiveData<List<CoffeeModel>>()
    val cart: LiveData<List<CoffeeModel>>
        get() = _cart

    //private val customerRef = fireStoreDb.collection("customers").document(FirebaseAuth.getInstance().currentUser!!.uid)

//    private val _favoriteIds = MutableLiveData<List<String>>()
//    val favoriteIds: LiveData<List<String>>
//        get() = _favoriteIds
//
//    private val _cartIds = MutableLiveData<List<String>>()
//    val cartIds: LiveData<List<String>>
//        get() = _cartIds

    private fun fetchFavoritesAndCart() {
        customerRef?.addSnapshotListener { snapshot, error ->
            if (error != null) {
                println("Error fetching favorite and cart IDs: $error")
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val customerData = snapshot.toObject<CustomerData>() ?: CustomerData()
                _favorites.value = customerData.favorites
                _cart.value = customerData.cart
                _saveForLater.value = customerData.saved_for_later
                _myOrders.value = customerData.my_orders

                //val allIds = customerData.favorites.union(customerData.cart).union(customerData.saved_for_later).toList()

//                fireStoreDb.collection("owners")
//                    .get()
//                    .addOnSuccessListener { result ->
//                        val allProductsList = mutableListOf<CoffeeModel>()
//                        for (document in result) {
//                            val ownerData = document.toObject<OwnerData>()
//                            val ownerProducts = ownerData.products.filter { it.coffeeId in allIds }
//                            allProductsList.addAll(ownerProducts)
//                        }
//                        val favoritesList = allProductsList.filter { it.coffeeId in customerData.favorites }
//                        _favorites.value = favoritesList
//
//                        val cartList = allProductsList.filter { it.coffeeId in customerData.cart }
//                        _cart.value = cartList
//
//
//                        val savedForLaterList = allProductsList.filter { it.coffeeId in customerData.saved_for_later}
//                        _saveForLater.value = savedForLaterList

//                        val myOrderList = allProductsList.filter { it.coffeeId in customerData.my_orders }
//                        _myOrders.value = myOrderList

//                        var _originalTotalPrice = 0.0
//                        var _currentTotalPrice = 0.0
//                        _cart.value!!.forEach {
//                            _originalTotalPrice += it.originalPrice.toDoubleOrNull() ?: 0.0
//                            _currentTotalPrice += it.currentPrice.toDoubleOrNull() ?: 0.0
//                        }
//                        totalOriginalPrice.value = _originalTotalPrice
//                        totalCurrentPrice.value = _currentTotalPrice
//                    }
//                    .addOnFailureListener { exception ->
//                        // Handle any errors
//                        println("Error getting documents: $exception")
//                    }
            }
        }
    }

    init {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            customerRef = fireStoreDb.collection("customers").document(currentUser.uid)
            fetchFavoritesAndCart()
        } else {
            // Handle case when user is not signed in
            // For example, you could show a login screen or take appropriate action
        }
    }
}

data class CustomerData(
    val favorites: List<CoffeeModel> = emptyList(),
    val cart: List<CoffeeModel> = emptyList(),
    val saved_for_later: List<CoffeeModel> = emptyList(),
    val my_orders: List<MyOrderItem>? = emptyList()
)

//"cart"
//"favorites"
//"saved_for_later"
//"my_orders"