package com.tp.rd.bot.accuweather

import com.tp.rd.bot.RainDanceBot
import com.tp.rd.discord.DiscordClient
import com.tp.rd.discord.model.DiscordProps
import com.tp.rd.weather.accuweather.AccuWeatherClient
import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import sx.blah.discord.api.IDiscordClient

object AccuWeatherRainDanceBot {
  def apply()(implicit config: Config): RainDanceBot = {
    val discordProps: DiscordProps = config.as[DiscordProps]("discord")
    apply(DiscordClient(discordProps))
  }

  def apply(discordClient: IDiscordClient)
           (implicit config: Config): RainDanceBot = {
    val weatherClient = new AccuWeatherClient()
    new RainDanceBot(discordClient, weatherClient)
  }
}

