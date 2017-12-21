package com.tp.rd.bot

import java.util.concurrent.ConcurrentHashMap
import java.util.{Calendar, Timer, TimerTask}

import com.tp.rd.bot.WeatherClientTask.forecastText
import com.tp.rd.weather.WeatherClient
import com.tp.rd.weather.model.{Location, WeatherBoost}
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.{IChannel, IMessage}
import sx.blah.discord.util.MessageBuilder

import scala.collection.JavaConverters._
import scala.concurrent.duration._

class WeatherClientTask(discordClient: IDiscordClient,
                        weatherClient: WeatherClient,
                        weatherRequestMinute: Int = 45,
                        messagePeriod: FiniteDuration = 1.hour) extends TimerTask {
  private val activeChannelMap = new ConcurrentHashMap[IChannel, Location]()
  private val timer: Timer = new Timer()

  def start(): Unit = timer.schedule(this, firstTime.getTime, messagePeriod.toMillis)

  override def run(): Unit = {
    val activeChannels = activeChannelMap.asScala.groupBy(_._2).mapValues(_.keys)
    messageDiscordChannels(activeChannels)
  }

  def messageDiscordChannels(activeChannels: Map[Location, Iterable[IChannel]]): Unit = {
    activeChannels.foreach { case (location, channels) =>
      val contentOpt = weatherClient.hourly(1, location.locationKey).headOption
      contentOpt.foreach { weatherBoost =>
        channels.foreach { channel =>
          broadcastForecast(weatherBoost, channel)
        }
      }
    }
  }

  protected def broadcastForecast(weatherBoost: WeatherBoost, channel: IChannel): IMessage = {
    val forecast: String = forecastText(weatherBoost)
    new MessageBuilder(discordClient)
      .withChannel(channel)
      .withContent(forecast)
      .build()
  }

  def startLocation(channel: IChannel, location: Location): Location = activeChannelMap.put(channel, location)

  def stopOnServer(channel: IChannel): Location = activeChannelMap.remove(channel)

  private def firstTime: Calendar = {
    val cal = Calendar.getInstance
    cal.set(Calendar.MINUTE, weatherRequestMinute)
    cal
  }

  override def cancel(): Boolean = {
    timer.cancel()
    super.cancel()
  }
}

object WeatherClientTask {
  val ContentDateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("HH:mm")

  def forecastText(weatherBoost: WeatherBoost): String = {
    s"Forecast for ${ContentDateTimeFormatter.print(weatherBoost.time)} is ${weatherBoost.weatherBoost}."
  }
}
