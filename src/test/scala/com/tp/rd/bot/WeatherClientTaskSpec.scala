package com.tp.rd.bot

import java.util.Calendar
import java.util.concurrent.ConcurrentHashMap

import com.tp.rd.weather.WeatherClient
import com.tp.rd.weather.accuweather.model.AccuLocation
import com.tp.rd.weather.model.WeatherBoostValue.WeatherBoostValue
import com.tp.rd.weather.model.{Location, WeatherBoost, WeatherBoostValue}
import org.joda.time.DateTime
import org.scalatest.{FlatSpec, Matchers, PrivateMethodTester}
import org.specs2.mock.Mockito
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.internal.json.objects.EmbedObject
import sx.blah.discord.handle.obj.{IChannel, IMessage}

class WeatherClientTaskSpec extends FlatSpec with Matchers with Mockito with PrivateMethodTester {

  case class MockWeatherBoost(weatherBoost: WeatherBoostValue, time: DateTime) extends WeatherBoost

  behavior of "run"
  it should "not call messageDiscordChannels if there are no active channels" in {
    val discordClient = mock[IDiscordClient]
    val weatherClient = mock[WeatherClient]
    val weatherClientTask = new WeatherClientTask(discordClient, weatherClient) {
      override def messageDiscordChannels(activeChannels: Map[Location, Iterable[IChannel]]): Unit = {
        fail("Should not reach here.")
      }
    }
    weatherClientTask.run()
  }

  it should "call messageDiscordChannels on active channels" in {
    val channel = mock[IChannel]
    val location = AccuLocation("LocationKey")

    val discordClient = mock[IDiscordClient]
    val weatherClient = mock[WeatherClient]
    val weatherClientTask = new WeatherClientTask(discordClient, weatherClient) {
      override def messageDiscordChannels(activeChannels: Map[Location, Iterable[IChannel]]): Unit = {
        activeChannels should contain key location
        activeChannels(location) should contain(channel)
      }
    }
    weatherClientTask.startLocation(channel, location)
    weatherClientTask.run()
  }

  behavior of "broadcastForecast"
  it should "broadcast the expected message" in {
    val weatherBoost = MockWeatherBoost(WeatherBoostValue.Rain, new DateTime())
    val message = mock[IMessage]
    val channel = mock[IChannel]
    channel.sendMessage(anyString, any[EmbedObject](), anyBoolean).returns(message)
    val discordClient = mock[IDiscordClient]
    val weatherClient = mock[WeatherClient]
    val weatherClientTask = new WeatherClientTask(discordClient, weatherClient)
    val broadcastForecast = PrivateMethod[IMessage]('broadcastForecast)
    weatherClientTask invokePrivate broadcastForecast(weatherBoost, channel)
    val expectedContent = WeatherClientTask.forecastText(weatherBoost)
    there was one(channel).sendMessage(expectedContent, null, false)
  }

  behavior of "startLocation"
  it should "add channel/location entry to activeChannelMap" in {
    val channel = mock[IChannel]
    val location = AccuLocation("LocationKey")

    val discordClient = mock[IDiscordClient]
    val weatherClient = mock[WeatherClient]
    val weatherClientTask = new WeatherClientTask(discordClient, weatherClient)
    val activeChannelMap = PrivateMethod[ConcurrentHashMap[IChannel, Location]]('activeChannelMap)
    weatherClientTask invokePrivate activeChannelMap() shouldBe empty
    weatherClientTask.startLocation(channel, location)
    weatherClientTask invokePrivate activeChannelMap() should contain key channel
  }

  behavior of "stopOnServer"
  it should "remove channel/location entry to activeChannelMap" in {
    val channel = mock[IChannel]
    val location = AccuLocation("LocationKey")

    val discordClient = mock[IDiscordClient]
    val weatherClient = mock[WeatherClient]
    val weatherClientTask = new WeatherClientTask(discordClient, weatherClient)
    val activeChannelMap = PrivateMethod[ConcurrentHashMap[IChannel, Location]]('activeChannelMap)
    weatherClientTask.startLocation(channel, location)
    weatherClientTask invokePrivate activeChannelMap() should contain key channel
    weatherClientTask.stopOnServer(channel)
    weatherClientTask invokePrivate activeChannelMap() shouldBe empty
  }

  behavior of "firstTime"
  it should "set the correct minute to run the task on" in {
    val requestMinute = 15
    val discordClient = mock[IDiscordClient]
    val weatherClient = mock[WeatherClient]
    val weatherClientTask = new WeatherClientTask(discordClient, weatherClient, requestMinute)
    val firstTimeMethod = PrivateMethod[Calendar]('firstTime)
    val calendar = weatherClientTask invokePrivate firstTimeMethod()
    calendar.get(Calendar.MINUTE) should be(requestMinute)
  }

  behavior of "forecastText"
  it should "" in {
    val weatherBoost = MockWeatherBoost(WeatherBoostValue.Rain, new DateTime())
    val forecastText = WeatherClientTask.forecastText(weatherBoost)
    forecastText should include(WeatherClientTask.ContentDateTimeFormatter.print(weatherBoost.time))
    forecastText should include(weatherBoost.weatherBoost.toString)

  }
}
