package com.twframe

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.danielasfregola.twitter4s.TwitterClient
import com.twframe.Entities.StreamComments
import com.typesafe.config.ConfigFactory

/**
 * Created by gerardo.mendez on 7/27/16.
 */

object Main extends App with Service with Core with Twitter with Comments {
  override implicit val system = ActorSystem("twframe")
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()
  override implicit val twitterClient = TwitterClient()

  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)

  Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port"))

  (twitter ! StreamComments("youtube"))(comments)
}