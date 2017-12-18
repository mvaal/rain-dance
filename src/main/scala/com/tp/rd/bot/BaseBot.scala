package com.tp.rd.bot

import sx.blah.discord.api.IDiscordClient

trait BaseBot {
  val discordClient: IDiscordClient
}