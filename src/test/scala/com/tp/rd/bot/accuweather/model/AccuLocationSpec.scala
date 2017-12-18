package com.tp.rd.bot.accuweather.model

import com.tp.rd.weather.accuweather.model.AccuLocation
import org.scalatest.{FlatSpec, Matchers}

class AccuLocationSpec extends FlatSpec with Matchers {
  behavior of "locationKey"
  it should "be the same value as the AccuLocation key" in {
    val expectedKey = "ExpectedKey"
    val accuLocation = AccuLocation(expectedKey)
    accuLocation.locationKey should be(expectedKey)
  }
}
