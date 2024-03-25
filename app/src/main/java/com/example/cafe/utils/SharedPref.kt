package com.example.cafe.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

object SharedPref {

    private fun storeGeneralData(
        isCustomer: Boolean,
        context: Context
    ) {
        val sharedPreferences = context.getSharedPreferences("general", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isCustomer", isCustomer)
        editor.apply()
    }

    fun getIsCustomer(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("general", MODE_PRIVATE)
        return sharedPreferences.getBoolean("isCustomer", true)
    }



    fun storeData(
        imgUrl: String,
        name: String,
        email: String,
        uid: String,
        context: Context
    ) {
        storeGeneralData(isCustomer = true, context = context)
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("imgUrl", imgUrl)
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("uid", uid)
        editor.apply()
    }

    fun getImgUrl(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("imgUrl", "")!!
    }
    fun getName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("name", "")!!
    }
    fun getEmail(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("email", "")!!
    }
    fun getUid(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("uid", "")!!
    }


    // Owner

    fun storeOwnerData(
        imgUrl: String,
        name: String,
        cafeName: String,
        location: String,
        licenceNumber: String,
        email: String,
        uid: String,
        context: Context
    ) {
        storeGeneralData(isCustomer = false, context = context)
        val sharedPreferences = context.getSharedPreferences("owners", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("imgUrl", imgUrl)
        editor.putString("name", name)
        editor.putString("cafeName", cafeName)
        editor.putString("location", location)
        editor.putString("licence", licenceNumber)
        editor.putString("email", email)
        editor.putString("uid", uid)
        editor.apply()
    }

    fun getOwnerImgUrl(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("owners", MODE_PRIVATE)
        return sharedPreferences.getString("imgUrl", "")!!
    }
    fun getOwnerName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("owners", MODE_PRIVATE)
        return sharedPreferences.getString("name", "")!!
    }
    fun getOwnerCafeName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("owners", MODE_PRIVATE)
        return sharedPreferences.getString("cafeName", "")!!
    }
    fun getOwnerLocation(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("owners", MODE_PRIVATE)
        return sharedPreferences.getString("location", "")!!
    }
    fun getOwnerLicenceNumber(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("owners", MODE_PRIVATE)
        return sharedPreferences.getString("licenceNumber", "")!!
    }
    fun getOwnerEmail(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("owners", MODE_PRIVATE)
        return sharedPreferences.getString("email", "")!!
    }
    fun getOwnerUid(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("owners", MODE_PRIVATE)
        return sharedPreferences.getString("uid", "")!!
    }

}