package com.tp.rd.bot.accuweather

import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, Matchers}
import org.specs2.mock.Mockito
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.EventDispatcher

class AccuWeatherRainDanceBotSpec
  extends FlatSpec
    with Matchers
    with Mockito {
  behavior of "apply"

  it should "properly create an unstarted RainDanceBot" in {
    val discordClient = mock[IDiscordClient]
    val dispatcher = mock[EventDispatcher]
    discordClient.getDispatcher.returns(dispatcher)
    doNothing.when(dispatcher).registerListener(discordClient)

    val rainDanceBot = AccuWeatherRainDanceBot(discordClient)(ConfigFactory.load())
    rainDanceBot.weatherClientTask.scheduledExecutionTime() should be(0)
  }
}