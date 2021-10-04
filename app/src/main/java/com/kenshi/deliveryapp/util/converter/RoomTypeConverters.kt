package com.kenshi.deliveryapp.util.converter

import androidx.room.TypeConverter

object RoomTypeConverters {

    @TypeConverter
    @JvmStatic
    //string 으로 반환해서 저장할 수 있도록
    fun toString(pair: Pair<Int, Int>): String {
        return "${pair.first},${pair.second}"
    }

    @TypeConverter
    @JvmStatic
    //string 값으로 가져온 것을 pair 로 변환
    fun toIntPair(str: String): Pair<Int, Int> {
        val splitedStr = str.split(",")
        return Pair(Integer.parseInt(splitedStr[0]), Integer.parseInt(splitedStr[1]))
    }

}