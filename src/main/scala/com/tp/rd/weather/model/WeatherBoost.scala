package com.tp.rd.weather.model

import com.tp.rd.weather.model.WeatherBoostValue.WeatherBoostValue
import org.joda.time.DateTime

trait WeatherBoost {
  val weatherBoost: WeatherBoostValue

  val time: DateTime
}

object WeatherBoostValue extends Enumeration {
  type WeatherBoostValue = Value
  val Clear, Rain, `Partly Cloudy`, Cloudy, Windy, Snow, Fog, Extreme = Value
}