package com.kenshi.deliveryapp.data.repository.restaurant

import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantEntity
import com.kenshi.deliveryapp.data.network.MapApiService
import com.kenshi.deliveryapp.screen.home.restaurant.RestaurantCategory
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

//String resource 들은 resourceProvider 등을 통해 주입받을 필요가 있음
//resourceProvider 가 String resource 를 꺼내옴
class DefaultRestaurantRepository(
    //주입받아 사용
    private val mapApiService: MapApiService,
    private val resourcesProvider: ResourcesProvider,
    private val ioDispatchers: CoroutineDispatcher
) : RestaurantRepository {

    override suspend fun getList(
        restaurantCategory: RestaurantCategory,
        locationLatLngEntity: LocationLatLngEntity
    ): List<RestaurantEntity> = withContext(ioDispatchers) {
        //Mock data 가 아닌 api 를 통해 data 를 불러오도록 구현

        //Poi 라고 하는 데이터가 있고 이것을 기반으로 List 형태로 불러옴
        //endpoint 호출
        val response = mapApiService.getSearchLocationAround(
            categories = resourcesProvider.getString(restaurantCategory.categoryTypeId),
            centerLat = locationLatLngEntity.latitude.toString(),
            centerLon = locationLatLngEntity.longitude.toString(),
            searchType = "name",
            //반경
            radius = "1",
            //특정 스펙
            resCoordType = "EPSG3857",
            searchtypCd = "A",
            //특정 타입
            reqCoordType = "WGS84GEO"
        )

        if(response.isSuccessful) {
            response.body()?.searchPoiInfo?.pois?.poi?.map { poi ->
                RestaurantEntity(
                    id = hashCode().toLong(),
                    restaurantInfoId = (1..10).random().toLong(),
                    restaurantCategory = restaurantCategory,
                    restaurantTitle = poi.name ?: "제목 없음",
                    //poi 에서 이미지는 제공해주지 않아서 랜덤 이미지 이용
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() * ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair((0..20).random(), (40..60).random()),
                    deliveryTipRange = Pair((0..1000).random(), (2000..4000).random()),
                    restaurantTelNumber = poi.telNo
                )
            } ?: listOf()
        } else {
            listOf()
        }
    }
}
            //Mocking data

            /*
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
            )         */
