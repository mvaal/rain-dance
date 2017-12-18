package com.tp.rd.bot

import java.util.concurrent.ConcurrentHashMap
import java.util.{Calendar, Timer, TimerTask}

import com.tp.rd.bot.RainDanceBot._
import com.tp.rd.weather.WeatherClient
import com.tp.rd.weather.model.Location
import org.joda.time.format.DateTimeFormat
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.util.MessageBuilder

import scala.collection.JavaConverters._

//w!everyday set 12:30
class RainDanceBot(val discordClient: IDiscordClient, weatherClient: WeatherClient)
  extends BaseBot
    with DiscordListener {
  private val activeChannelMap = new ConcurrentHashMap[IChannel, Location]()
  new Timer().schedule(new WeatherClientTask, firstTime.getTime, 1000 * 60 * 60)


  private class WeatherClientTask extends TimerTask {
    override def run(): Unit = {
      val activeChannels = activeChannelMap.asScala.groupBy(_._2).mapValues(_.keys)
      if (activeChannels.nonEmpty) {
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
    }
  }

  override def handleEvent(event: MessageReceivedEvent): Unit = {
    val message = event.getMessage
    val channel = event.getChannel
    val author = event.getAuthor
    if (message.getContent.startsWith(COMMAND_START)) {
      val Array(_, command) = message.getContent.split(COMMAND_SEPARATOR)
      val content = command.split(" ") match {
        case Array(START_COMMAND, startCommand) =>
          val Array(lat, lon) = startCommand.split(",").map(_.trim.toDouble)
          val location = weatherClient.location(lat, lon)
          activeChannelMap.put(channel, location)
          s"I have started to check for weather changes at ($lat,$lon)."
        case Array(STOP_COMMAND) =>
          val location = activeChannelMap.remove(channel)
          s"I have stopped detecting weather changes for ${location.locationKey}."
        case Array(COMMANDS_COMMAND) =>
          COMMANDS.map(command => s"$COMMAND_SEPARATOR$command").mkString("\n")
        case _ =>
          s"Not a valid command, $author. For help, type ${COMMAND_START}commands."
      }
      new MessageBuilder(discordClient)
        .withChannel(channel)
        .withContent(content)
        .build()
    }
  }

  private def firstTime: Calendar = {
    val cal = Calendar.getInstance
    cal.set(Calendar.MINUTE, 45)
    cal
  }
}

object RainDanceBot {
  val COMMAND_SEPARATOR: String = "!"
  val COMMAND_START: String = s"rd$COMMAND_SEPARATOR"
  val COMMANDS_COMMAND: String = "commands"
  val START_COMMAND: String = "start"
  val STOP_COMMAND: String = "stop"

  val COMMANDS = List(
    START_COMMAND,
    STOP_COMMAND,
    COMMANDS_COMMAND
  )
}