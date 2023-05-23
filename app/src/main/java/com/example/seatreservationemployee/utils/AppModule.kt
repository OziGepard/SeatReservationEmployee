package com.example.seatreservationemployee.utils

import com.example.seatreservationemployee.db.CinemaFirebaseDatabase
import com.example.seatreservationemployee.retrofit.DateAPI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth
    {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideDateAPI(): DateAPI =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_DATE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DateAPI::class.java)

    @Singleton
    @Provides
    fun provideFirebaseInstance(): FirebaseFirestore
    {
        return FirebaseFirestore.getInstance()
    }
    @Singleton
    @Provides
    fun provideFirebaseDatabase(dbFBInstance: FirebaseFirestore): CinemaFirebaseDatabase {
        return CinemaFirebaseDatabase(dbFBInstance)
    }

}