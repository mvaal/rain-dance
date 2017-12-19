package com.tp.rd.weather.accuweather.model

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonProperty}
import com.tp.rd.weather.accuweather.model.AccuForecast.{dateTimeFormatter, iconPhaseToWeatherBoost}
import com.tp.rd.weather.model.WeatherBoostValue.WeatherBoostValue
import com.tp.rd.weather.model.{WeatherBoost, WeatherBoostValue}
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}
import org.joda.time.{DateTime, DateTimeZone}

case class AccuForecast(@JsonProperty("DateTime") dateTimeStr: String,
                        @JsonProperty("EpochDateTime") epochDateTimeSeconds: Long,
                        @JsonProperty("WeatherIcon") weatherIcon: Int,
                        @JsonProperty("IconPhrase") iconPhase: String,
                        @JsonProperty("IsDaylight") isDaylight: Boolean,
                        @JsonProperty("Temperature") temperature: AccuTemperature,
                        @JsonProperty("PrecipitationProbability") precipitationProbability: Int,
                        @JsonProperty("MobileLink") mobileLink: String,
                        @JsonProperty("Link") link: String) extends WeatherBoost {
  @JsonIgnore
  val dateTime: DateTime = dateTimeFormatter.parseDateTime(dateTimeStr)

  @JsonIgnore
  val epochDateTime: DateTime = new DateTime(epochDateTimeSeconds * 1000)

  @JsonIgnore
  override val weatherBoost: WeatherBoostValue = iconPhaseToWeatherBoost(iconPhase)

  @JsonIgnore
  override val time: DateTime = dateTime
}

object AccuForecast {
  val dateTimeFormatter: DateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis().withZone(DateTimeZone.getDefault)

  def iconPhaseToWeatherBoost(iconPhase: String): WeatherBoostValue = {
    iconPhase match {
      case "Cloudy" => WeatherBoostValue.Cloudy
      case "Showers" => WeatherBoostValue.Rain
      case "Rain" => WeatherBoostValue.Rain
      case "Mostly cloudy" => WeatherBoostValue.Cloudy
      case "Intermittent clouds" => WeatherBoostValue.`Partly Cloudy`
      case "Partly sunny" => WeatherBoostValue.Clear
      case "Fog" => WeatherBoostValue.Fog
      case _ => throw new RuntimeException(s"Unknown iconPhase: $iconPhase")
    }
  }
}