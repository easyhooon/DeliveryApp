package com.kenshi.deliveryapp.data.response.search

import com.kenshi.deliveryapp.data.response.search.Pois

data class SearchPoiInfo(
    val totalCount: String,
    val count: String,
    val page: String,
    val pois: Pois
)
