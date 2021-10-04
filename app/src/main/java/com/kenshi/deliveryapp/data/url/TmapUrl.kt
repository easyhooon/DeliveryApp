package com.kenshi.deliveryapp.data.url

object Url {
    //Base url
    const val TMAP_URL = "https://apis.openapi.sk.com"

    //end point 1
    const val GET_TMAP_POIS = "/tmap/pois"
    //end point 2
    const val GET_TMAP_POIS_AROUND = "/tmap/pois/search/around"
    //end point 3
    const val GET_TMAP_REVERSE_GEO_CODE = "/tmap/geo/reversegeocoding"

    // 메뉴 리스트 mock api url
    const val FOOD_URL = "https://60abc8f15a4de40017ccae3e.mockapi.io/"
}
