package com.kenshi.deliveryapp.data.repository.restaurant.food

import com.kenshi.deliveryapp.data.db.dao.FoodMenuBasketDao
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.kenshi.deliveryapp.data.network.FoodApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

//레포지토리 구현체
class DefaultRestaurantFoodRepository(
    private val foodApiService: FoodApiService,
    //Dao 를 주입받음
    private val foodMenuBasketDao: FoodMenuBasketDao,
    private val ioDispatcher: CoroutineDispatcher
): RestaurantFoodRepository {

    override suspend fun getFoods(restaurantId: Long): List<RestaurantFoodEntity> = withContext(ioDispatcher) {
        val response = foodApiService.getRestaurantFoods(restaurantId)
        if(response.isSuccessful) {
            response.body()?.map{
                it.toEntity(restaurantId) } ?: listOf()
            } else {
                listOf()
            }
        }

    override suspend fun getAllFoodMenuListInBasket(): List<RestaurantFoodEntity> = withContext(ioDispatcher) {
        foodMenuBasketDao.getAll()
    }

    override suspend fun getFoodMenuListInBasket(restaurantId: Long): List<RestaurantFoodEntity> = withContext(ioDispatcher) {
        foodMenuBasketDao.getAllByRestaurantId(restaurantId)
    }

    override suspend fun insertFoodMenuInBasket(restaurantFoodEntity: RestaurantFoodEntity) = withContext(ioDispatcher) {
        foodMenuBasketDao.insert(restaurantFoodEntity)
    }

    override suspend fun removeFoodMenuListInBasket(foodId: String) = withContext(ioDispatcher) {
        foodMenuBasketDao.delete(foodId)
    }

    override suspend fun clearFoodMenuListInBasket() = withContext(ioDispatcher) {
        foodMenuBasketDao.deleteAll()
    }
}