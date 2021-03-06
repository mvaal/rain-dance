package com.tp.rd.bot.accuweather.model

import java.io.ByteArrayInputStream

import com.tp.rd.weather.accuweather.model.AccuForecast.dateTimeFormatter
import com.tp.rd.weather.accuweather.model.{AccuForecast, AccuTemperature, ObjectMapper}
import com.tp.rd.weather.model.WeatherBoostValue
import com.tp.rd.weather.model.WeatherBoostValue.WeatherBoostValue
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{FlatSpec, Matchers}

class AccuForecastSpec extends FlatSpec with Matchers {
  behavior of "iconPhaseToWeatherBoost"

  it should "have the correct expected response" in {
    val phases = Table(
      ("iconPhase", "weatherBoost"),
      ("Cloudy", WeatherBoostValue.Cloudy),
      ("Showers", WeatherBoostValue.Rain),
      ("Rain", WeatherBoostValue.Rain),
      ("Mostly cloudy", WeatherBoostValue.Cloudy),
      ("Intermittent clouds", WeatherBoostValue.`Partly Cloudy`),
      ("Partly sunny", WeatherBoostValue.Clear),
      ("Fog", WeatherBoostValue.Fog)
    )
    forAll(phases) { (iconPhase: String, weatherBoost: WeatherBoostValue) =>
      AccuForecast.iconPhaseToWeatherBoost(iconPhase) should be(weatherBoost)
    }
  }

  it should "throw an Exception if an invalid weather is passed" in {
    an[RuntimeException] should be thrownBy AccuForecast.iconPhaseToWeatherBoost("WrongWeather")
  }

  behavior of "weatherBoost"

  it should "return the expected WeatherBoost properties" in {
    val accuTemperature = AccuTemperature(36, "F", 18)
    val accuForecast = AccuForecast(
      "2017-12-16T01:00:00-06:00",
      1513407600,
      7,
      "Cloudy",
      isDaylight = false,
      accuTemperature,
      0,
      "http://url",
      "http://url"
    )
    accuForecast.weatherBoost should be(AccuForecast.iconPhaseToWeatherBoost(accuForecast.iconPhase))
    accuForecast.time should be(dateTimeFormatter.parseDateTime(accuForecast.dateTimeStr))
  }

  behavior of "parseAccuForecasts"

  it should "parse properly from InputStream" in {
    val accuTemperature = AccuTemperature(36, "F", 18)
    val accuForecast = AccuForecast(
      "2017-12-16T01:00:00-06:00",
      1513407600,
      7,
      "Cloudy",
      isDaylight = false,
      accuTemperature,
      0,
      "http://url",
      "http://url"
    )
    val expectedResponse = Array(accuForecast)
    val byteArray = ObjectMapper.writeValueAsBytes(expectedResponse)
    val inputStream = new ByteArrayInputStream(byteArray)
    AccuForecast.parseAccuForecasts(inputStream) should be(expectedResponse)
  }

  it should "parse properly from String" in {
    val accuTemperature = AccuTemperature(36, "F", 18)
    val accuForecast = AccuForecast(
      "2017-12-16T01:00:00-06:00",
      1513407600,
      7,
      "Cloudy",
      isDaylight = false,
      accuTemperature,
      0,
      "http://url",
      "http://url"
    )
    val expectedResponse = Array(accuForecast)
    val jsonString = ObjectMapper.writeValueAsString(expectedResponse)
    AccuForecast.parseAccuForecasts(jsonString) should be(expectedResponse)
  }
}
