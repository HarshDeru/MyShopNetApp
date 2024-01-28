package com.example.myshopnetapp.Utilities

// This is a sealed class that defines the different types of resources that can be returned from a network call.
sealed class Resource<T>(
// It is a generic class that can take any type of data.
    val data :T?=null,
    val message:String?=null
) {

    // The "Success" subclass represents a successful network call and contains the returned data.
    class Success<T>(data: T?) : Resource<T>(data)
    // The "Error" subclass represents a failed network call and contains the error message.
    class Error<T>(message: String?) : Resource<T>(null,message)
    // The "Loading" subclass represents a network call that is still in progress.
    class Loading<T> : Resource<T>()
    // The "Unspecified" subclass represents an unspecified state of the resource.
    class Unspecified<T> : Resource<T>()
}