package com.tp.rd.weather.accuweather.model

import com.fasterxml.jackson.annotation.JsonProperty

case class AccuTemperature(@JsonProperty("Value") value: Int,
                           @JsonProperty("Unit") unit: String,
                           @JsonProperty("UnitType") unitType: Int)
