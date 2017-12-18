package com.tp.rd.sample

import com.tp.rd.weather.accuweather.AccuWeatherClient

object AccuWeatherClientExample extends App {
  val accuWeatherClient = new AccuWeatherClient()
  val location = accuWeatherClient.location(30.2672, -97.7431)
  println(location)
  println(accuWeatherClient.hourly(1, location.locationKey).mkString("\n"))
  println()
}
