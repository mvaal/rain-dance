package com.tp.rd.weather

import com.tp.rd.weather.model.{Location, WeatherBoost}

trait WeatherClient {
  def hourly(hours: Int, locationKey: String): Seq[WeatherBoost]

  def location(lat: Double, lon: Double): Location
}
