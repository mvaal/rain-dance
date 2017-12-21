package com.tp.rd.apps

import com.tp.rd.bot.accuweather.AccuWeatherRainDanceBot
import com.tp.rd.discord.DiscordClient
import com.tp.rd.discord.model.ConfigDiscordProps
import com.typesafe.config.{Config, ConfigFactory}

object AccuWeatherRainDanceBotRunner extends App {
  private implicit val config: Config = ConfigFactory.load()
  val rainDanceBot = AccuWeatherRainDanceBot(DiscordClient(ConfigDiscordProps()))
  rainDanceBot.start()
}
