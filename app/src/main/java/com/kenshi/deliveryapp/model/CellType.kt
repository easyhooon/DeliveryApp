package com.kenshi.deliveryapp.model

//식당의 대한 아이템, 메뉴의 대한 아이템, 리뷰 글의 대한 아이템
enum class CellType {
    EMPTY_CELL,
    RESTAURANT_CELL,
    LIKE_RESTAURANT_CELL,
    FOOD_CELL,
    REVIEW_CELL,
    ORDER_FOOD_CELL,
    ORDER_CELL
}