package com.tp.rd.bot.accuweather

import com.tp.rd.bot.RainDanceBot
import com.tp.rd.weather.accuweather.AccuWeatherClient
import com.typesafe.config.Config
import sx.blah.discord.api.IDiscordClient

object AccuWeatherRainDanceBot {
  def apply(discordClient: IDiscordClient)
           (implicit config: Config): RainDanceBot = {
    val weatherClient = new AccuWeatherClient()
    new RainDanceBot(discordClient, weatherClient)
  }
}

