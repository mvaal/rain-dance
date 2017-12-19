package com.tp.rd.bot

import org.scalatest.{FlatSpec, Matchers}
import org.specs2.mock.Mockito
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.EventDispatcher
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

class DiscordListenerSpec extends FlatSpec with Matchers with Mockito {

  class TestBot(val discordClient: IDiscordClient) extends BaseBot

  behavior of "handle"
  it should "should pass the message to handleEvent when successful" in {
    val discordClient: IDiscordClient = {
      val discordClient = mock[IDiscordClient]
      val dispatcher = mock[EventDispatcher]
      discordClient.getDispatcher.returns(dispatcher)
      doNothing.when(dispatcher).registerListener(discordClient)
      discordClient
    }

    val messageReceivedEvent = mock[MessageReceivedEvent]
    val discordListener = new TestBot(discordClient) with DiscordListener {
      override def handleEvent(event: MessageReceivedEvent): Unit = {
        event should be(messageReceivedEvent)
      }
    }
    discordListener.handle(messageReceivedEvent)
  }

  it should "should not throw exception on handleEvent failures" in {
    val discordClient: IDiscordClient = {
      val discordClient = mock[IDiscordClient]
      val dispatcher = mock[EventDispatcher]
      discordClient.getDispatcher.returns(dispatcher)
      doNothing.when(dispatcher).registerListener(discordClient)
      discordClient
    }

    val messageReceivedEvent = mock[MessageReceivedEvent]
    val discordListener = new TestBot(discordClient) with DiscordListener {
      override def handleEvent(event: MessageReceivedEvent): Unit = {
        event should be(messageReceivedEvent)
        throw new RuntimeException()
      }
    }
    discordListener.handle(messageReceivedEvent)
  }
}
