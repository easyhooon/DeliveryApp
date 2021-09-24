package com.kenshi.deliveryapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.kenshi.deliveryapp.data.db.ApplicationDatabase

//database build
fun provideDB(context: Context): ApplicationDatabase =
    Room.databaseBuilder(
        context,
        ApplicationDatabase::class.java,
        ApplicationDatabase.DB_NAME
    ).build()

fun provideLocationDao(database: ApplicationDatabase) = database.locationDao()