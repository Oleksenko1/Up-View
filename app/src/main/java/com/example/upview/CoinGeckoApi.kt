package com.example.upview

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApi {
    @GET("coins/markets")
    fun getCoinsMarkets(
        @Query("vs_currency") vsCurrency: String,
        @Query("ids") ids: String,
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false
    ): Call<List<CoinMarket>>

    @GET("coins/{id}/market_chart")
    fun getMarketChart(
        @retrofit2.http.Path("id") id: String,
        @Query("vs_currency") vsCurrency: String,
        @Query("days") days: String // "1", "7", "30", "365" и т.п.
    ): Call<MarketChartResponse>
}
