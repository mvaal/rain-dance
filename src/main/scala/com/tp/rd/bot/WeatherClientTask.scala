package com.tp.rd.bot

import java.io.Closeable
import java.util.concurrent.ConcurrentHashMap
import java.util.{Calendar, Timer, TimerTask}

import com.tp.rd.weather.WeatherClient
import com.tp.rd.weather.model.Location
import org.joda.time.format.DateTimeFormat
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.util.MessageBuilder

import scala.collection.JavaConverters._
import scala.concurrent.duration._

class WeatherClientTask(discordClient: IDiscordClient,
                        weatherClient: WeatherClient,
                        weatherRequestMinute: Int = 45,
                        messagePeriod: FiniteDuration = 1.hour) extends TimerTask with Closeable {
  private val activeChannelMap = new ConcurrentHashMap[IChannel, Location]()
  private val timer: Timer = new Timer()

  def start(): Unit = timer.schedule(this, firstTime.getTime, messagePeriod.toMillis)

  override def run(): Unit = {
    val activeChannels = activeChannelMap.asScala.groupBy(_._2).mapValues(_.keys)
    if (activeChannels.nonEmpty) {
      messageDiscordChannels(activeChannels)
    }
  }

  def messageDiscordChannels(activeChannels: Map[Location, Iterable[IChannel]]): Unit = {
    activeChannels.foreach { case (location, channels) =>
      val contentOpt = weatherClient.hourly(1, location.locationKey).headOption
      contentOpt.foreach { content =>
        channels.foreach { channel =>
          val formatter = DateTimeFormat.forPattern("HH:mm")
          val forecast = s"Forecast for ${formatter.print(content.time)} is ${content.weatherBoost}."
          new MessageBuilder(discordClient)
            .withChannel(channel)
            .withContent(forecast)
            .build()
        }
      }
    }
  }

  def startLocation(channel: IChannel, location: Location): Location = activeChannelMap.put(channel, location)

  def stopOnServer(channel: IChannel): Location = activeChannelMap.remove(channel)

  private def firstTime: Calendar = {
    val cal = Calendar.getInstance
    cal.set(Calendar.MINUTE, weatherRequestMinute)
    cal
  }

  override def close(): Unit = timer.cancel()
}
