package com.example.upview

data class CoinMarket(
    val id: String,
    val symbol: String,
    val name: String,
    val current_price: Double,
    val market_cap_rank: Int,
    val image: String? = null
)
