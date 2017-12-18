package com.tp.rd.weather.accuweather

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.tp.rd.weather.WeatherClient
import com.tp.rd.weather.accuweather.AccuWeatherClient.{Host, objectMapper}
import com.tp.rd.weather.accuweather.model.{AccuForecast, AccuLocation, AccuWeatherProps}
import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

import scalaj.http.Http

class AccuWeatherClient(private val config: Config = ConfigFactory.load("accuweather.conf")) extends WeatherClient {
  private val props = config.as[AccuWeatherProps]("accuweather")

  override def hourly(hours: Int, locationKey: String): Seq[AccuForecast] = {
    assert(hours == 1 || hours == 12, "Supported hours are 1 and 12.")
    val forecastResponse = Http(s"$Host/forecasts/v1/hourly/${hours}hour/$locationKey")
      .param("apikey", props.apikey)
      .execute(parser = { inputStream =>
        objectMapper.readValue(inputStream, classOf[Array[AccuForecast]])
      })
    forecastResponse.body
  }

  def location(lat: Double, lon: Double): AccuLocation = {
    val forecastResponse = Http(s"$Host/locations/v1/cities/geoposition/search")
      .params(("apikey", props.apikey), ("q", s"$lat,$lon"))
      .execute(parser = { inputStream =>
        objectMapper.readValue(inputStream, classOf[AccuLocation])
      })
    forecastResponse.body
  }
}

object AccuWeatherClient {
  val objectMapper: ObjectMapper = (new ObjectMapper() with ScalaObjectMapper)
    .registerModule(DefaultScalaModule)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  val Host: String = "http://dataservice.accuweather.com"
}
