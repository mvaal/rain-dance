package com.tp.rd.bot

import java.io.Closeable

import com.tp.rd.bot.RainDanceBot._
import com.tp.rd.weather.WeatherClient
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.util.MessageBuilder

class RainDanceBot(val discordClient: IDiscordClient, weatherClient: WeatherClient)
  extends BaseBot
    with DiscordListener
    with Closeable {

  val weatherClientTask = new WeatherClientTask(discordClient, weatherClient)

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
          weatherClientTask.startLocation(channel, location)
          s"I have started to check for weather changes at ($lat,$lon)."
        case Array(STOP_COMMAND) =>
          val location = weatherClientTask.stopOnServer(channel)
          s"I have stopped detecting weather changes for ${location.locationKey}."
        case Array(COMMANDS_COMMAND) =>
          COMMANDS.map { case (cmd, params) => s"$COMMAND_SEPARATOR$cmd $params" }.mkString("\n")
        case _ =>
          s"Not a valid command, $author. For help, type ${COMMAND_START}commands."
      }
      new MessageBuilder(discordClient)
        .withChannel(channel)
        .withContent(content)
        .build()
    }
  }

  def start(): Unit = weatherClientTask.start()

  override def close(): Unit = weatherClientTask.cancel()
}

object RainDanceBot {
  val COMMAND_SEPARATOR: String = "!"
  val COMMAND_START: String = s"rd$COMMAND_SEPARATOR"
  val COMMANDS_COMMAND: String = "commands"
  val START_COMMAND: String = "start"
  val STOP_COMMAND: String = "stop"

  val COMMANDS = Map(
    START_COMMAND -> "<latitude>,<longitude>",
    STOP_COMMAND -> "",
    COMMANDS_COMMAND -> ""
  )
}