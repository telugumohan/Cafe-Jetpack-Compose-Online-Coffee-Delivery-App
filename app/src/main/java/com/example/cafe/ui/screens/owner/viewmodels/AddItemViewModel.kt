package com.example.cafe.ui.screens.owner.viewmodels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cafe.model.CoffeeModel
import com.example.cafe.viewmodel.OwnerOrderItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AddItemViewModel: ViewModel() {

    private val fireStoreDb = Firebase.firestore

    private val currentOwnerId = FirebaseAuth.getInstance().currentUser!!.uid

    private val ownerCollectionRef = fireStoreDb.collection("owners").document(currentOwnerId)

    private val storageRef: StorageReference = Firebase.storage.reference
    private val imageRef = storageRef.child("products/${UUID.randomUUID()}.jpg")

    companion object {
        private const val TAG = "OwnerAddItemViewModel"
    }

    fun addProductToOwner(imgUri: Uri, ownerId: String = currentOwnerId, product: CoffeeModel, navigateToHome: () -> Unit) {
        // Generate a unique coffeeId
        val coffeeId = UUID.randomUUID().toString()

        // doom doom
        val uploadTask = imageRef.putFile(imgUri)

        uploadTask.addOnSuccessListener { uploadTaskSnapshot ->
            // Get the download URL for the image
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()

                // Set the coffeeId and imgUrl for the product
                val productWithId = product.copy(coffeeId = coffeeId, imgUrl = imageUrl)

                // Update Firestore document with the product
                ownerCollectionRef.update("products", FieldValue.arrayUnion(productWithId))
                    .addOnSuccessListener {
                        Log.d(TAG, "Product added to owner's list successfully")
                        navigateToHome()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error adding product to owner's list: ", e)
                    }
            }
        }
            .addOnFailureListener { exception ->
                // Handle any errors during the upload
                Log.e(TAG, "Error uploading image: ", exception)
            }
    }


    private val _products = MutableLiveData<List<CoffeeModel>>()
    val products: LiveData<List<CoffeeModel>>
        get() = _products

    private val _ownerOrders = MutableLiveData<List<OwnerOrderItem>>()
    val ownerOrders: LiveData<List<OwnerOrderItem>>
        get() = _ownerOrders

    private fun getProductsAndOrders() {
        ownerCollectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val ownerData = snapshot.toObject<OwnerData>()
                val productsList = ownerData?.products.orEmpty()
                _products.value = productsList
                val ownerOrdersList = ownerData?.cafe_orders.orEmpty()
                _ownerOrders.value = ownerOrdersList
            }
        }
    }

    init {
        getProductsAndOrders()
    }


}

data class OwnerData(
    val products: List<CoffeeModel> = emptyList(),
    val cafe_orders: List<OwnerOrderItem> = emptyList()
)
