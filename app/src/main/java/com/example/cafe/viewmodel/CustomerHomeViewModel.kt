package com.example.cafe.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cafe.model.CoffeeModel
import com.example.cafe.ui.screens.owner.viewmodels.OwnerData
import com.example.cafe.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class CustomerHomeViewModel: ViewModel() {
    private val fireStoreDb = Firebase.firestore
    private var customerRef: DocumentReference? = null

    private val _allProducts = MutableLiveData<List<CoffeeModel>>()
    val allProducts: LiveData<List<CoffeeModel>>
        get() = _allProducts

    init {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            customerRef = fireStoreDb.collection("customers").document(currentUser.uid)
        } else {
            // Handle case when user is not signed in
            // For example, you could show a login screen or take appropriate action
        }
        fetchAllProducts()
    }

    private fun fetchAllProducts() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            fireStoreDb.collection("owners")
                .get()
                .addOnSuccessListener { result ->
                    val allProductsList = mutableListOf<CoffeeModel>()
                    for (document in result) {
                        val ownerData = document.toObject<OwnerData>()
                        allProductsList.addAll(ownerData.products)
                    }
                    _allProducts.value = allProductsList
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                    println("Error getting documents: $exception")
                }
        }
    }

    private val _coffeeModelById = MutableLiveData<CoffeeModel?>()
    val coffeeModelById: LiveData<CoffeeModel?>
        get() = _coffeeModelById

    fun fetchCoffeeModel(coffeeId: String) {
     if (FirebaseAuth.getInstance().currentUser != null) {
         fireStoreDb.collection("owners")
             .get()
             .addOnSuccessListener {result ->
                 for (document in result) {
                     val ownerData = document.toObject<OwnerData>()
                     val myCoffeeModel = ownerData.products.find {
                         it.coffeeId == coffeeId
                     }
                     if (myCoffeeModel != null) {
                         _coffeeModelById.value = myCoffeeModel
                         return@addOnSuccessListener
                     }
                 }
                 _coffeeModelById.value = null
             }
             .addOnFailureListener{exception ->
                 // Error Occurred...
                 println("Error getting documents: $exception")
                 _coffeeModelById.value = null
             }
     }
    }
//fun fetchCoffeeModel(coffeeId: String) {
//    fireStoreDb.collection("owners")
//        .whereArrayContains("products", mapOf("coffeeId" to coffeeId))
//        .get()
//        .addOnSuccessListener { result ->
//            if (!result.isEmpty) {
//                val ownerData = result.documents[0].toObject<OwnerData>()
//                val coffeeModel = ownerData?.products?.find { it.coffeeId == coffeeId }
//                _coffeeModelById.value = coffeeModel
//            } else {
//                // CoffeeModel with the provided coffeeId not found
//                _coffeeModelById.value = null
//            }
//        }
//        .addOnFailureListener { exception ->
//            // Handle any errors
//            println("Error getting documents: $exception")
//            _coffeeModelById.value = null
//        }
//}
   // private val customerRef = fireStoreDb.collection("customers").document(FirebaseAuth.getInstance().currentUser!!.uid)
    fun addToFavorites(coffeeModel: CoffeeModel) {
    customerRef?.update("favorites", FieldValue.arrayUnion(coffeeModel))
        ?.addOnSuccessListener {
            Log.d("CustomerHomeViewModel", "Added to favorites.")
        }
        ?.addOnFailureListener{
            Log.d("CustomerHomeViewModel", "Not able to add to favorites")
        }
    }
    fun addToCart(coffeeModel: CoffeeModel) {
        customerRef?.update("cart", FieldValue.arrayUnion(coffeeModel))
            ?.addOnSuccessListener {
                Log.d("CustomerHomeViewModel", "Added to cart.")
            }
            ?.addOnFailureListener{
                Log.d("CustomerHomeViewModel", "Not able to add to cart.")
            }
    }

    fun removeFromFavorites(coffeeModel: CoffeeModel) {
        customerRef?.update("favorites", FieldValue.arrayRemove(coffeeModel))
            ?.addOnSuccessListener {
                Log.d("CustomerHomeViewModel", "Removed from favorites.")
            }
            ?.addOnFailureListener{
                Log.d("CustomerHomeViewModel", "Not able to remove from favorites.")
            }
    }

    fun removeFromCart(coffeeModel: CoffeeModel) {
        customerRef?.update("cart", FieldValue.arrayRemove(coffeeModel))
            ?.addOnSuccessListener {
                Log.d("CustomerHomeViewModel", "Removed from cart.")
            }
            ?.addOnFailureListener{
                Log.d("CustomerHomeViewModel", "Not able to remove from cart.")
            }
    }

    fun addToSaveForLater(coffeeModel: CoffeeModel) {
        removeFromCart(coffeeModel)
        customerRef?.update("saved_for_later", FieldValue.arrayUnion(coffeeModel))
            ?.addOnSuccessListener {
                Log.d("CustomerHomeViewModel", "Added to Saved for later.")
            }
            ?.addOnFailureListener{
                Log.d("CustomerHomeViewModel", "Not able to add to Saved for later.")
            }
    }
    fun removeToSaveForLater(coffeeModel: CoffeeModel) {
        addToCart(coffeeModel)
        customerRef?.update("saved_for_later", FieldValue.arrayRemove(coffeeModel))
            ?.addOnSuccessListener {
                Log.d("CustomerHomeViewModel", "Removed from Saved for later.")
            }
            ?.addOnFailureListener{
                Log.d("CustomerHomeViewModel", "Not able to remove from Saved for later.")
            }
    }

    val checkOutState = MutableStateFlow(CheckOutState())
    fun updateCheckOutState(myOrderItemList: List<MyOrderItem>) {
        checkOutState.update { currentState ->
            currentState.copy(myOrderItems = myOrderItemList)
        }
    }

    fun addToOrders(myOrderItemList: List<MyOrderItem>, context: Context) {

        myOrderItemList.forEach {
            val myOrderItemId = UUID.randomUUID().toString()
            removeFromCart(it.coffeeModel)
            val ownerOrderItem = OwnerOrderItem(
                coffeeModel = it.coffeeModel,
                qty = it.qty,
                customerName = SharedPref.getName(context),
                orderId = myOrderItemId
            )
            addToOwnerOrders(ownerOrderItem = ownerOrderItem)
            customerRef?.update("my_orders", FieldValue.arrayUnion(it.copy(myOrderItemId = myOrderItemId)))
                ?.addOnSuccessListener {
                    Log.d("CustomerHomeViewModel", "Added to Orders.")
                }
                ?.addOnFailureListener {
                    Log.d("CustomerHomeViewModel", "Not able to add to Orders.")
                }
        }
    }

    private fun addToOwnerOrders(ownerOrderItem: OwnerOrderItem) {
        if (ownerOrderItem.coffeeModel.ownerId.isNotEmpty()) {
            val ownerRef = fireStoreDb.collection("owners").document(ownerOrderItem.coffeeModel.ownerId)
            ownerRef.update("cafe_orders", FieldValue.arrayUnion(ownerOrderItem))
                .addOnSuccessListener {
                    Log.d("CustomerHomeViewModel", "Added to Owner Orders.")
                }
                .addOnFailureListener {
                    Log.d("CustomerHomeViewModel", "Not able to add to Owner Orders.")
                }
        }
    }

}

//"cart"
//"favorites"
//"saved_for_later"
//"my_orders"

data class OwnerOrderItem(
    val coffeeModel: CoffeeModel = CoffeeModel(),
    val qty: Int = 1,
    val customerName: String = "",
    val deliveryLocation: String = "Kurnool",
    val orderId: String = ""
)