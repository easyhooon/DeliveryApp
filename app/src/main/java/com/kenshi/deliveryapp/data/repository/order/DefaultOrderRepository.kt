package com.kenshi.deliveryapp.data.repository.order

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.kenshi.deliveryapp.data.entity.OrderEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantFoodEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DefaultOrderRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val firestore: FirebaseFirestore
): OrderRepository {

    override suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        foodMenuList: List<RestaurantFoodEntity>,
        restaurantTitle: String
    ): Result = withContext(ioDispatcher) {
        val result: Result
        val orderMenuData = hashMapOf(
            "restaurantId" to restaurantId,
            "userId" to userId,
            "orderMenuList" to foodMenuList,
            "restaurantTitle" to restaurantTitle
        )

        result = try {
            firestore.collection("order")
                .add(orderMenuData)
            Result.Success<Any>()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
        return@withContext result

    }

    override suspend fun getAllOrderMenus(userId: String): Result = withContext(ioDispatcher) {
        return@withContext try {
            //원하는 데이터(주문 데이터)를 snapshot 의 형태로 받아옴
            val result: QuerySnapshot = firestore
                .collection("order")
                    //필드내에 같은 값이 있는지 비교
                .whereEqualTo("userId", userId)
                .get()
                .await()
            Result.Success(result.documents.map{
                //id 는 snapshot 의 id (order 문서내의 id 토큰 값) 그냥 넣어주면 됨
                OrderEntity(
                    id = it.id,
                    userId = it.get("userId") as String,
                    restaurantId = it.get("restaurantId") as Long,
                    foodMenuList = (it.get("orderMenuList") as ArrayList<Map<String, Any>>).map { food ->
                        RestaurantFoodEntity(
                            id = food["id"] as String,
                            title = food["title"] as String,
                            description = food["description"] as String,
                            price = (food["price"] as Long).toInt(),
                            imageUrl = food["imageUrl"] as String,
                            restaurantId = food["restaurant"] as Long,
                            restaurantTitle = food["restaurantTitle"] as String
                        )
                    },
                    restaurantTitle = it.get("restaurantTitle") as String
                )
            })
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
    }

    //주문 요청의 대한 result
    sealed class Result {
        data class Success<T> (
            val data: T? = null
        ): Result()

        data class Error(
            val e: Throwable
        ): Result()
    }
}