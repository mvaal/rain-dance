package com.tp.rd.weather.accuweather.model

import java.io.InputStream

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonProperty}
import com.tp.rd.weather.model.Location

case class AccuLocation(@JsonProperty("Key") key: String) extends Location {
  @JsonIgnore
  override val locationKey: String = key
}

object AccuLocation {
  def parseAccuLocation(inputStream: InputStream): AccuLocation = {
    ObjectMapper.readValue(inputStream, classOf[AccuLocation])
  }
}
