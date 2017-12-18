package com.tp.rd.discord

import com.tp.rd.discord.model.DiscordProps
import sx.blah.discord.api.{ClientBuilder, IDiscordClient}

import scala.util.{Failure, Success, Try}

object DiscordClient {
  def apply(props: DiscordProps)
           (implicit clientBuilder: ClientBuilder = new ClientBuilder()): IDiscordClient = {
    val builder = clientBuilder.withToken(props.token)
    Try {
      builder.login()
    } match {
      case Success(client) => client
      case Failure(ex) =>
        System.err.println("Error occurred while logging in")
        throw ex
    }
  }
}
