package com.tp.rd.weather.accuweather.model

import org.scalatest.{FlatSpec, Matchers}

class modelSpec extends FlatSpec with Matchers {
  behavior of "ObjectMapper"

  it should "not fail on unknown properties" in {
    val expectedKey = "ExpectedKey"
    val sampleLocation =
      s"""
         |{
         |    "Version": 1,
         |    "Key": "$expectedKey",
         |    "Type": "City"
         |}
      """.stripMargin
    val accuLocation = ObjectMapper.readValue(sampleLocation, classOf[AccuLocation])
    accuLocation.key should be(expectedKey)
  }
}
