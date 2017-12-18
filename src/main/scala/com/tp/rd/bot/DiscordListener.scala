package com.tp.rd.bot

import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.util.{DiscordException, MissingPermissionsException, RateLimitException}

trait DiscordListener extends IListener[MessageReceivedEvent] {
  this: BaseBot =>
  discordClient.getDispatcher.registerListener(this)

  override def handle(event: MessageReceivedEvent): Unit = {
    try {
      handleEvent(event)
    } catch {
      case ex: RateLimitException => ex.printStackTrace()
      case ex: DiscordException => ex.printStackTrace()
      case ex: MissingPermissionsException => ex.printStackTrace()
    }
  }

  def handleEvent(event: MessageReceivedEvent): Unit
}
