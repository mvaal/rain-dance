package com.tp.rd.discord

import com.tp.rd.discord.model.DiscordProps
import org.scalatest.{FlatSpec, Matchers}
import org.specs2.mock.Mockito
import sx.blah.discord.api.{ClientBuilder, IDiscordClient}

class DiscordClientSpec extends FlatSpec with Matchers with Mockito {
  behavior of "apply"

  it should "return the expected Discord client" in {
    val expectedToken = "ExpectedToken"
    val props = DiscordProps(expectedToken)
    val clientBuilder = mock[ClientBuilder]
    val discordClient = mock[IDiscordClient]
    clientBuilder.withToken(expectedToken).returns(clientBuilder)
    clientBuilder.login().returns(discordClient)
    DiscordClient(props)(clientBuilder) should be(discordClient)
  }

  it should "throw an exception if login throws an exception" in {
    val exceptionMsg = "ExceptionMsg"
    val expectedToken = "ExpectedToken"
    val props = DiscordProps(expectedToken)
    val clientBuilder = mock[ClientBuilder]
    clientBuilder.withToken(expectedToken).returns(clientBuilder)
    clientBuilder.login().throws(new RuntimeException(exceptionMsg))
    the[RuntimeException] thrownBy DiscordClient(props)(clientBuilder) should have message exceptionMsg
  }
}
