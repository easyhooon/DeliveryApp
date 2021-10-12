package com.kenshi.deliveryapp.data.repository.restaurant.review

import com.google.firebase.firestore.FirebaseFirestore
import com.kenshi.deliveryapp.data.entity.ReviewEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DefaultRestaurantReviewRepository(
    private val ioDispatcher: CoroutineDispatcher,
    //firestore 주입
    private val firestore: FirebaseFirestore
): RestaurantReviewRepository {

    override suspend fun getReviews(restaurantTitle: String): Result = withContext(ioDispatcher) {
        /*
        //mock data
        return@withContext (0..10).map {

            RestaurantReviewEntity(
                id = 0,
                title = "제목 $it",
                description = "내용 $it",
                grade = (1 until 5).random(),
            )
        }
         */

        //Firestore 에서 data 를 가져옴
        return@withContext try {
            val snapshot = firestore
                .collection("review")
                .whereEqualTo("restaurantTitle", restaurantTitle)
                .get()
                    //coroutine 으로 가져옴
                .await()
            Result.Success(snapshot.documents.map {
                ReviewEntity(
                    userId = it.get("userId") as String,
                    title = it.get("title") as String,
                    createdAt = it.get("createdAt") as Long,
                    content = it.get("content") as String,
                    rating = (it.get("content") as Double).toFloat(),
                    //사진은 없을 수 있기 때문에
                    imageUrlList = it.get("imageUrlList") as? List<String>,
                    orderId = it.get("orderId") as String,
                    restaurantTitle = it.get("orderId") as String
                )
            })
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
    }

    sealed class Result {
        data class Success<T>(
            val data: T? = null
        ): Result()

        data class Error(
            val e: Throwable
        ): Result()
    }
}