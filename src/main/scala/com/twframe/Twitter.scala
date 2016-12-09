package com.twframe

import akka.actor._
import akka.pattern.pipe
import com.danielasfregola.twitter4s.TwitterClient
import com.danielasfregola.twitter4s.entities._

import scala.concurrent.ExecutionContextExecutor
import Entities._

/**
 * Created by gerardo.mendez on 7/30/16.
 */
trait Twitter {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val twitterClient: TwitterClient

  lazy val twitter = system.actorOf(Twitter.props, "twitter")

  val urlRegex =
    """^.*(?:youtu.be\/|twfra.me\/|embed\/|\?v=|\&v=)([^#\&\?]{11})(?:(?:\?t=|\&t=|\?start=|\&start=)(\d*))?.*$""".r

  def tweetToComment(tweet: Tweet): Option[Comment] = {
    tweet.lang match {
      case Some("en") =>
        tweet.entities.flatMap(_.urls.headOption.map(_.expanded_url).map {
          case urlRegex(video, time) =>
            Some(Comment(tweet.id_str, video, tweet.created_at.getTime, Option(time).map(_.toInt), tweet.in_reply_to_status_id_str))
          case _ => None
        }) flatten
      case _ => None
    }
  }

  object Twitter {
    def props = Props(new Twitter())
  }

  class Twitter extends Actor with ActorLogging {

    def receive = {
      case SearchComments(query: String) =>
        twitterClient.searchTweet(query, count = 100).map(_.statuses flatMap tweetToComment) pipeTo sender

      case StreamComments(query: String) =>
        val processor = context.actorOf(TwitterStreamProcessor.props(sender))
        twitterClient.getStatusesFilter(track = Some(query))(processor)
    }
  }

  object TwitterStreamProcessor {
    def props(requester: ActorRef) = Props(new TwitterStreamProcessor(requester))
  }

  class TwitterStreamProcessor(requester: ActorRef) extends Actor with ActorLogging {
    def receive = {
      case StreamingUpdate(tweet: Tweet) =>
        tweetToComment(tweet).foreach (comment => (requester ! comment) (context.parent))

      case StreamingUpdate(limitNotice: LimitNotice) =>

      case StreamingUpdate(update: StreamingMessage) =>
        log.debug("Streaming update: {}", update)
    }
  }
}