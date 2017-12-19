package com.tp.rd.bot

import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

trait DiscordListener extends IListener[MessageReceivedEvent] {
  this: BaseBot =>
  discordClient.getDispatcher.registerListener(this)

  override def handle(event: MessageReceivedEvent): Unit = {
    try {
      handleEvent(event)
    } catch {
      case ex: Throwable => ex.printStackTrace()
    }
  }

  def handleEvent(event: MessageReceivedEvent): Unit
}
