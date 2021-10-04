package com.kenshi.deliveryapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kenshi.deliveryapp.data.db.dao.FoodMenuBasketDao
import com.kenshi.deliveryapp.data.db.dao.LocationDao
import com.kenshi.deliveryapp.data.db.dao.RestaurantDao
import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantFoodEntity

@Database(
    entities = [LocationLatLngEntity::class, RestaurantEntity::class, RestaurantFoodEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase() {

    companion object {
        const val DB_NAME = "ApplicationDataBase.db"
    }

    abstract fun LocationDao(): LocationDao

    abstract fun RestaurantDao(): RestaurantDao

    abstract fun FoodMenuBasketDao(): FoodMenuBasketDao
}