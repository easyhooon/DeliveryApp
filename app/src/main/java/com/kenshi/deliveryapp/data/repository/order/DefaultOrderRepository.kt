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

    companion object {
        const val ID = "id"
        const val USER_ID = "userId"
        const val RESTAURANT_ID = "restaurantId"
        const val RESTAURANT_TITLE = "restaurantTitle"
        const val ORDER_MENU_LIST = "orderMenuList"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val PRICE = "price"
        const val IMAGE_URL = "imageUrl"
        const val ORDER = "order"
    }

    override suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        foodMenuList: List<RestaurantFoodEntity>,
        restaurantTitle: String
    ): Result = withContext(ioDispatcher) {
        val result: Result
        val orderMenuData = hashMapOf(
            RESTAURANT_ID to restaurantId,
            USER_ID to userId,
            ORDER_MENU_LIST to foodMenuList,
            RESTAURANT_TITLE to restaurantTitle
        )

        result = try {
            firestore
                .collection(ORDER)
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
                    userId = it.get(USER_ID) as String,
                    restaurantId = it.get(RESTAURANT_ID) as Long,
                    foodMenuList = (it.get(ORDER_MENU_LIST) as ArrayList<Map<String, Any>>).map { food ->
                        RestaurantFoodEntity(
                            id = food[ID] as String,
                            title = food[TITLE] as String,
                            description = food[DESCRIPTION] as String,
                            price = (food[PRICE] as Long).toInt(),
                            imageUrl = food[IMAGE_URL] as String,
                            restaurantId = food[RESTAURANT_ID] as Long,
                            restaurantTitle = food[RESTAURANT_TITLE] as String
                        )
                    },
                    restaurantTitle = it.get(RESTAURANT_TITLE) as String
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