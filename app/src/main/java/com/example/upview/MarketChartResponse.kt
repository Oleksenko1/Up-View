package com.example.upview

data class MarketChartResponse(
    val prices: List<List<Double>>, // [ [timestamp, price], ... ]
    val market_caps: List<List<Double>>,
    val total_volumes: List<List<Double>>
)
