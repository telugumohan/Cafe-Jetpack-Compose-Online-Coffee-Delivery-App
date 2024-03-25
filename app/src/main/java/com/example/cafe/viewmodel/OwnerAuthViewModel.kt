package com.example.cafe.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cafe.model.CoffeeModel
import com.example.cafe.model.OwnerModel
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class OwnerAuthViewModel: ViewModel() {

    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    val ownerRef = db.getReference("owners")



    private val storageRef: StorageReference = Firebase.storage.reference
    private val imageRef = storageRef.child("owners/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    var signUpInProgress = mutableStateOf(false)

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun ownerLogin(email: String, password: String, context: Context) {
        signUpInProgress.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                    getOwnerData(uid = auth.currentUser!!.uid, context)
                    signUpInProgress.value = false
                }
                else {
                    _error.postValue(it.exception!!.message)
                    signUpInProgress.value = false
                }
            }
    }
    private fun getOwnerData(uid: String, context: Context) {
        ownerRef.child(uid).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val ownerData = snapshot.getValue(OwnerModel::class.java)
                SharedPref.storeOwnerData(
                    imgUrl = ownerData!!.imgUrl,
                    name = ownerData.name,
                    cafeName = ownerData.cafeName,
                    location = ownerData.location,
                    licenceNumber = ownerData.licenceNumber,
                    email = ownerData.email,
                    uid = ownerData.uid,
                    context = context
                )
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun ownerRegister(
        imgUrl: Uri,
        name: String,
        cafeName: String,
        location:String,
        licenceNumber: String,
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
                        cafeName = cafeName,
                        location = location,
                        licenceNumber = licenceNumber,
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
        cafeName: String,
        location:String,
        licenceNumber: String,
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
                    cafeName = cafeName,
                    location = location,
                    licenceNumber = licenceNumber,
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
        cafeName: String,
        location:String,
        licenceNumber: String,
        email: String,
        password: String,
        uid: String?,
        context: Context
    ) {
         //FIREBASE
        val fireStoreDb = Firebase.firestore
        val ownersRef = fireStoreDb.collection("owners").document(_firebaseUser.value!!.uid)

        val data = hashMapOf(
            "products" to listOf<CoffeeModel>(),
            "cafe_orders" to listOf<OwnerOrderItem>()
        )
        ownersRef.set(data)
        // CLOSE FIREBASE

        val ownerData = OwnerModel(imgUrl = imgUrl, name = name, cafeName = cafeName, location = location, licenceNumber = licenceNumber, email = email, password = password, uid = uid!!)
        ownerRef.child(uid).setValue(ownerData)
            .addOnSuccessListener {
                SharedPref.storeOwnerData(
                    imgUrl = imgUrl,
                    name = name,
                    cafeName = cafeName,
                    location = location,
                    licenceNumber = licenceNumber,
                    email = email,
                    uid = uid,
                    context = context
                )
            }
            .addOnFailureListener {
                _error.postValue(it.message)
            }
    }

    private  val TAG = OwnerAuthViewModel::class.simpleName
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