package com.twframe

import akka.actor.{ActorLogging, Actor, Props, ActorSystem}
import com.twframe.Entities._

import scala.collection.mutable
import scala.concurrent.ExecutionContextExecutor

/**
 * Created by gerardo.mendez on 8/17/16.
 */
trait Comments {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor

  lazy val comments = system.actorOf(Comments.props, "comments")

  object Comments {
    def props = Props(new Comments())
  }

  class Comments extends Actor with ActorLogging {

    val videos: mutable.Map[String,mutable.TreeSet[Comment]] = mutable.HashMap[String,mutable.TreeSet[Comment]]()

    def getComments(video: String): mutable.TreeSet[Comment] =
      videos.getOrElseUpdate(video, new mutable.TreeSet[Comment]()(Ordering.by(_.createdAt)))

    def receive = {
      case comment: Comment => getComments(comment.video) += comment

      case comments: List[_] if comments.forall(_.isInstanceOf[Comment]) =>
        comments.asInstanceOf[List[Comment]] foreach(c => getComments(c.video) += c)

      case GetPopularVideos(offset: Int, limit: Int) =>
        sender ! videos.toSeq.sortBy(_._2.size).reverse.slice(offset, offset + limit).map(_._1).toList

      case GetComments(video: String, limit: Int, after: Option[Long], before: Option[Long]) =>
        sender ! getComments(video).take(limit).toList
    }
  }
}
