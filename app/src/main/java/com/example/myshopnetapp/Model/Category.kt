package com.example.myshopnetapp.Model

// Sealed class Category with a primary constructor that takes a String parameter named category
sealed class Category(val category: String) {
    //this is how all category are named after on firebase
    object Hoodies: Category("Hoodies")
    object Jackets: Category ("Jackets")
    object TShirts: Category ("T-Shirts")
    object Jeans: Category ("Jeans")
    object Joggers: Category ("Joggers")
    object Shirts: Category ("Shirts")
}