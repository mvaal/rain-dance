package com.tp.rd.discord.model

import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, Matchers}

class ConfigDiscordPropsSpec extends FlatSpec with Matchers {
  behavior of "constructor"
  it should "properly read the config file" in {
    val discordProps = ConfigDiscordProps()(ConfigFactory.load())
    discordProps.token should be("DiscordApiKey")
  }
}
