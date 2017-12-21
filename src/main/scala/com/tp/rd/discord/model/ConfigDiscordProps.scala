package com.tp.rd.discord.model

import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

object ConfigDiscordProps {
  def apply()(implicit config: Config): DiscordProps = {
    config.as[DiscordProps]("discord")
  }
}
