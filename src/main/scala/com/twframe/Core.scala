package com.twframe

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.util.Timeout
import com.twframe.Entities._

import scala.concurrent._
import scala.concurrent.duration._

/**
 * Created by gerardo.mendez on 8/18/16.
 */
trait Core { this: Twitter with Comments =>

  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor

  private implicit val timeout: Timeout = Timeout(10.seconds)

  lazy val core = system.actorOf(Core.props, "core")

  object Core {
    def props = Props(new Core())
  }

  class Core extends Actor with ActorLogging {

    def receive = {
      case getComments: GetComments =>
        (comments ! getComments)(sender)
      case getPopularVideos: GetPopularVideos =>
        (comments ! getPopularVideos)(sender)
    }
  }



}
