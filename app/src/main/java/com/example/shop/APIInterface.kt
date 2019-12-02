package com.example.shop

import com.example.shop.buy.BuyBody
import com.example.shop.buy.BuyData
import com.example.shop.getSortItem.SortItem
import com.example.shop.getSortItem.SortItemData
import com.example.shop.login.LoginBody
import com.example.shop.login.LoginData
import com.example.shop.record.RecordData
import com.example.shop.renew.RenewData
import com.example.shop.res.ResBody
import com.example.shop.res.ResData
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

//    @Headers("Content-Type: application/json","Accept: application/json")


    @POST("/api/register")
    fun res(
        @Body body:ResBody
    ): Call<ResData>

    @POST("/api/login")
    fun login(
        @Body body:LoginBody
    ): Call<LoginData>

    @GET("/api/items/{sort_id}")
    fun getSortItem(
        @Path("sort_id") sort_id:Int
    ): Call<MutableList<SortItemData>>

    @POST("/api/sheepitem")
    fun buy(
        @Header("Authorization") token:String,
        @Body body:BuyBody
    ):Call<BuyData>

    @GET("/api/sheep/allbuy/{sheep_id}")
    fun record(
        @Header("Authorization") token:String,
        @Path("sheep_id") sheep_id:Int
    ):Call<RecordData>

    @GET("/api/sheepdata")
    fun renew(
        @Header("Authorization") token:String
    ):Call<RenewData>

}