package com.example.myshopnetapp.Utilities


import com.example.myshopnetapp.DatabaseFirestore.FirestoreCart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Module: This annotation indicates that the class is a Dagger module.
//@InstallIn: This annotation specifies the Android class in which the dependencies should be available.
//SingletonComponent: This is a predefined Hilt component that represents the singleton scope.
//object AppModule: This is an object class that defines the module.
//@Provides: This annotation indicates that the function provides a dependency.
//@Singleton: This annotation indicates that the dependency should be a singleton.
//fun provideFirebaseAuth(): This function provides an instance of the FirebaseAuth class.
//fun provideFirebaseFirestoreDatabase(): This function provides an instance of the FirebaseFirestore class.
//fun provideFirebaseCart(): This function provides an instance of the FirestoreCart class that requires an instance of the FirebaseAuth and FirebaseFirestore classes.

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestoreDatabase() = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseCart(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ) = FirestoreCart(firestore,firebaseAuth)

}