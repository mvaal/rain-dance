package com.tp.rd.bot.accuweather.model

import java.io.ByteArrayInputStream

import com.tp.rd.weather.accuweather.model.{AccuLocation, ObjectMapper}
import org.scalatest.{FlatSpec, Matchers}

class AccuLocationSpec extends FlatSpec with Matchers {
  behavior of "locationKey"
  it should "be the same value as the AccuLocation key" in {
    val expectedKey = "ExpectedKey"
    val accuLocation = AccuLocation(expectedKey)
    accuLocation.locationKey should be(expectedKey)
  }

  behavior of "parseAccuLocation"
  it should "parse properly from InputStream" in {
    val expectedKey = "ExpectedKey"
    val accuLocation = AccuLocation(expectedKey)
    val byteArray = ObjectMapper.writeValueAsBytes(accuLocation)
    val inputStream = new ByteArrayInputStream(byteArray)
    AccuLocation.parseAccuLocation(inputStream) should be(accuLocation)
  }
}
