package com.tp.rd.apps

import com.tp.rd.bot.accuweather.AccuWeatherRainDanceBot
import com.typesafe.config.ConfigFactory

object AccuWeatherRainDanceBotRunner extends App {
  val rainDanceBot = AccuWeatherRainDanceBot()(ConfigFactory.load())
  rainDanceBot.start()
}
