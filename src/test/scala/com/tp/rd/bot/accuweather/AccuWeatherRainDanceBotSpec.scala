package com.tp.rd.bot.accuweather

import java.time.LocalDateTime

import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, Matchers}
import org.specs2.mock.Mockito
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.EventDispatcher
import sx.blah.discord.api.internal.json.objects.EmbedObject
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.impl.obj.{Channel, Message, User}
import sx.blah.discord.handle.obj.IMessage.Type

import scala.collection.JavaConverters._

class AccuWeatherRainDanceBotSpec
  extends FlatSpec
    with Matchers
    with Mockito {
  behavior of "apply"

  it should "properly handle a Discord event" in {
    val expectedContent = "ExpectedContent"

    val discordClient = mock[IDiscordClient]
    val dispatcher = mock[EventDispatcher]
    discordClient.getDispatcher.returns(dispatcher)
    doNothing.when(dispatcher).registerListener(discordClient)

    val channel = mock[Channel]
    channel.sendMessage(expectedContent, null, false).returns(null)

    val messageReceived = messageEvent(expectedContent, discordClient, channel)
    val messageReceivedEvent = new MessageReceivedEvent(messageReceived)
    val rainDanceBot = AccuWeatherRainDanceBot(discordClient)(ConfigFactory.load())
    rainDanceBot.handleEvent(messageReceivedEvent)

    there was one(channel).sendMessage(anyString, any[EmbedObject](), anyBoolean)
  }

  private def messageEvent(content: String,
                           discordClient: IDiscordClient,
                           channel: Channel): Message = {
    new Message(
      discordClient,
      0L,
      content,
      mock[User],
      channel,
      LocalDateTime.now(),
      LocalDateTime.now(),
      true,
      List().asJava,
      List().asJava,
      List().asJava,
      true,
      List().asJava,
      List().asJava,
      0L,
      Type.DEFAULT
    )
  }
}