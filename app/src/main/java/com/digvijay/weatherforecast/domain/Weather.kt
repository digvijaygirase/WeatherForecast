package com.digvijay.weatherforecast.domain

data class Weather(
    val location: String,
    val temperature: String,
    val weatherDescription: String
)