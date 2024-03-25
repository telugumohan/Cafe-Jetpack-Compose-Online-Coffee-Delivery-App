package com.example.cafe.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cafe.model.CoffeeModel
import com.example.cafe.model.UserModel
import com.example.cafe.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AuthViewModel: ViewModel() {

    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("users")

    private val storageRef: StorageReference = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    var signUpInProgress = mutableStateOf(false)

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String, context: Context) {
        signUpInProgress.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                    getData(uid = auth.currentUser!!.uid, context)
                    signUpInProgress.value = false
                }
                else {
                    _error.postValue(it.exception!!.message)
                    signUpInProgress.value = false
                }
            }
    }
    private fun getData(uid: String, context: Context) {
        userRef.child(uid).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                SharedPref.storeData(
                    imgUrl = userData!!.imgUrl,
                    name = userData.name,
                    email = userData.email,
                    uid = userData.uid,
                    context = context
                )
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun register(
        imgUrl: Uri,
        name: String,
        email: String,
        password: String,
        context: Context
    ) {
        signUpInProgress.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    signUpInProgress.value = false
                    _firebaseUser.postValue(auth.currentUser)
                    saveImage(
                        imgUrl = imgUrl,
                        name = name,
                        email = email,
                        password = password,
                        uid = auth.currentUser?.uid,
                        context = context
                    )
                }
                else {
                    signUpInProgress.value = false
                    _error.postValue(it.exception!!.message)
                }
            }
    }

    private fun saveImage(
        imgUrl: Uri,
        name: String,
        email: String,
        password: String,
        uid: String?,
        context: Context
    ) {
        val uploadTask = imageRef.putFile(imgUrl)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData (
                    imgUrl = it.toString(),
                    name = name,
                    email = email,
                    password = password,
                    uid = uid,
                    context = context
                )
            }
        }
    }
    private fun saveData(
        imgUrl: String,
        name: String,
        email: String,
        password: String,
        uid: String?,
        context: Context
    ) {


//        customerRef.set(mapOf("cart" to listOf<String>())) // String -> coffeeId
//        customerRef.set(mapOf("favorites" to listOf<String>())) // String -> coffeeId

        // Firestore
        val fireStoreDb = Firebase.firestore
        val customerRef = fireStoreDb.collection("customers").document(_firebaseUser.value!!.uid)
        val data = hashMapOf(
            "cart" to listOf<CoffeeModel>(),
            "favorites" to listOf<CoffeeModel>(),
            "saved_for_later" to listOf<CoffeeModel>(),
            "my_orders" to listOf<MyOrderItem>()
        )
        customerRef.set(data)
            .addOnSuccessListener {
                Log.d("CustomerFavoritesCartViewModel", "Cart and favorites fields added successfully.")
            }
            .addOnFailureListener { e ->
                Log.e("CustomerFavoritesCartViewModel", "Error adding cart and favorites fields", e)
            }


        //Close Firestore


        val userData = UserModel(imgUrl = imgUrl, name = name, email = email, password = password, uid = uid!!)
        userRef.child(uid).setValue(userData)
            .addOnSuccessListener {
                SharedPref.storeData(imgUrl = imgUrl, name = name, email = email, uid = uid, context = context)
            }
            .addOnFailureListener {
                _error.postValue(it.message)
            }
    }

    private  val TAG = AuthViewModel::class.simpleName
    fun logout(navigateToChoice: () -> Unit) {
        auth.signOut()
        val authStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                _firebaseUser.postValue(null)
                Log.d(TAG, "Inside logout(): User logout successful.")
                navigateToChoice()
            } else {
                Log.d(TAG, "Inside logout(): User logout Unsuccessful.")
            }
        }
        auth.addAuthStateListener(authStateListener)
    }


}

//sealed interface OrderStatus {
//    data class InProgress(val title: String) : OrderStatus
//    data class GoingToPickUp(val title: String) : OrderStatus
//    data class Picked(val title: String): OrderStatus
//    data class Shipping(val title: String): OrderStatus
//    data class Delivered(val title: String): OrderStatus
//}

data class MyOrderItem(
    val coffeeModel: CoffeeModel = CoffeeModel(),
    val qty: Int = 0,
    val orderStatus: String = "Shipping",
    val myOrderItemId: String = ""
)

data class CheckOutState(
    val myOrderItems: List<MyOrderItem> = listOf()
)