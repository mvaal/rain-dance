package com.tp.rd.weather.accuweather

import java.io.InputStream

import com.tp.rd.weather.accuweather.AccuWeatherClient.ApikeyQueryParam
import com.tp.rd.weather.accuweather.model.{AccuForecast, AccuLocation}
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{FlatSpec, Matchers}
import org.specs2.mock.Mockito

import scala.util.Random
import scalaj.http.{HttpRequest, HttpResponse}

class AccuWeatherClientSpec extends FlatSpec with Matchers with Mockito {
  behavior of "hourly"

  it should "throw an exception if hour is not 0 or 12" in {
    val weatherClient = new AccuWeatherClient()
    a[AssertionError] should be thrownBy weatherClient.hourly(0, "")
  }

  it should "successfully return the expected array of AccuForecasts" in {
    val hoursTable = Table(
      "hours",
      1,
      12
    )
    forAll(hoursTable) { hours: Int =>
      val apiKey = "AccuWeatherApiKey"
      val locationKey = "LocationKey"
      val expectedResult = Array[AccuForecast]()
      val forecastResponse = mock[HttpResponse[Array[AccuForecast]]]
      forecastResponse.body.returns(expectedResult)
      val request = mock[HttpRequest]
      request.param(ApikeyQueryParam, apiKey).returns(request)
      request.execute(any[InputStream => Array[AccuForecast]]()).returns(forecastResponse)
      val weatherClient = new AccuWeatherClient() {
        override protected def httpRequest(url: String): HttpRequest = {
          url should be(AccuWeatherClient.hourlyUrl(hours, locationKey))
          request
        }
      }
      weatherClient.hourly(hours, locationKey) should be(expectedResult)
    }
  }

  it should "successfully return the expected AccuLocation" in {
    val expectedLat = Random.nextDouble()
    val expectedLon = Random.nextDouble()
    val apiKey = "AccuWeatherApiKey"
    val expectedResult = mock[AccuLocation]
    val forecastResponse = mock[HttpResponse[AccuLocation]]
    forecastResponse.body.returns(expectedResult)
    val request = mock[HttpRequest]
    request.params((ApikeyQueryParam, apiKey), ("q", s"$expectedLat,$expectedLon")).returns(request)
    request.execute(any[InputStream => AccuLocation]()).returns(forecastResponse)
    val weatherClient = new AccuWeatherClient() {
      override protected def httpRequest(url: String): HttpRequest = {
        url should be(AccuWeatherClient.locationUrl)
        request
      }
    }
    weatherClient.location(expectedLat, expectedLon) should be(expectedResult)
  }
}
