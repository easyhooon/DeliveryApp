package com.kenshi.deliveryapp.data.repository.restaurant

import com.kenshi.deliveryapp.data.entity.RestaurantEntity
import com.kenshi.deliveryapp.screen.home.restaurant.RestaurantCategory
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import kotlinx.coroutines.CoroutineDispatcher

//String resource 들은 resourceProvider 등을 통해 주입받을 필요가 있음
//resourceProvider 가 String resource 를 꺼내옴
class DefaultRestaurantRepository(
    //이 두가지를 주입받아 사용
    private val resourcesProvider: ResourcesProvider,
    private val ioDispatchers: CoroutineDispatcher
) : RestaurantRepository {

    override suspend fun getList(restaurantCategory: RestaurantCategory): List<RestaurantEntity> = when (restaurantCategory) {
        RestaurantCategory.ALL -> {

            //Mocking data
            listOf(
                RestaurantEntity(
                    id = 0,
                    restaurantInfoId = 0,
                    restaurantCategory = RestaurantCategory.ALL,
                    restaurantTitle = "마포화로집",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000)
                ),
                RestaurantEntity(
                    id = 1,
                    restaurantInfoId = 1,
                    restaurantCategory = RestaurantCategory.ALL,
                    restaurantTitle = "옛날우동&덮밥",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000)
                ),
                RestaurantEntity(
                    id = 2,
                    restaurantInfoId = 2,
                    restaurantCategory = RestaurantCategory.ALL,
                    restaurantTitle = "마스터석쇠불고기&냉면plus",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000)
                ),
                RestaurantEntity(
                    id = 3,
                    restaurantInfoId = 3,
                    restaurantCategory = RestaurantCategory.ALL,
                    restaurantTitle = "마스터통삼겹",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000)
                ),
                RestaurantEntity(
                    id = 4,
                    restaurantInfoId = 4,
                    restaurantCategory = RestaurantCategory.ALL,
                    restaurantTitle = "창영이 족발&보쌈",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000)
                ),
                RestaurantEntity(
                    id = 5,
                    restaurantInfoId = 5,
                    restaurantCategory = RestaurantCategory.ALL,
                    restaurantTitle = "콩나물국밥&코다리조림 콩심 인천논현점",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000)
                ),
                RestaurantEntity(
                    id = 6,
                    restaurantInfoId = 6,
                    restaurantCategory = RestaurantCategory.ALL,
                    restaurantTitle = "김여사 칼국수&냉면 논현점",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000)
                ),
                RestaurantEntity(
                    id = 7,
                    restaurantInfoId = 7,
                    restaurantCategory = RestaurantCategory.ALL,
                    restaurantTitle = "돈키호테",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000)
                ),
            )
        }
        RestaurantCategory.KOREAN_FOOD -> {
            listOf(
                RestaurantEntity(
                    id = 0,
                    restaurantInfoId = 0,
                    restaurantCategory = RestaurantCategory.KOREAN_FOOD,
                    restaurantTitle = "마포화로집",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000)
                ),
                RestaurantEntity(
                    id = 1,
                    restaurantInfoId = 1,
                    restaurantCategory = RestaurantCategory.KOREAN_FOOD,
                    restaurantTitle = "옛날우동&덮밥",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000)
                ),
                RestaurantEntity(
                    id = 2,
                    restaurantInfoId = 2,
                    restaurantCategory = RestaurantCategory.KOREAN_FOOD,
                    restaurantTitle = "마스터석쇠불고기&냉면plus",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000)
                ),
                RestaurantEntity(
                    id = 3,
                    restaurantInfoId = 3,
                    restaurantCategory = RestaurantCategory.KOREAN_FOOD,
                    restaurantTitle = "마스터통삼겹",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000)
                ),
                RestaurantEntity(
                    id = 4,
                    restaurantInfoId = 4,
                    restaurantCategory = RestaurantCategory.KOREAN_FOOD,
                    restaurantTitle = "창영이 족발&보쌈",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000)
                ),
                RestaurantEntity(
                    id = 5,
                    restaurantInfoId = 5,
                    restaurantCategory = RestaurantCategory.KOREAN_FOOD,
                    restaurantTitle = "콩나물국밥&코다리조림 콩심 인천논현점",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000)
                )
            )
        }
        else -> {
            listOf()
        }
    }

}