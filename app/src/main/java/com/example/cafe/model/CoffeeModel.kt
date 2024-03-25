package com.example.cafe.model

data class CoffeeModel (
    val coffeeId: String = "",
    val imgUrl: String = "",
    val title: String = "",
    val comboWith: String = "",
    val description: String = "",
    val rating: String = "",
    val peopleRatedCount: String = "",
    val originalPrice: String = "",
    val currentPrice: String = "",
    val isInStock: Boolean = false,
    val quantityAvail: Int = 0,
    val ownerId: String = ""
)
