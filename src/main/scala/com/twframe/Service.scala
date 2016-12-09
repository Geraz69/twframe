package com.twframe

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes.PermanentRedirect
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.twframe.Entities._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContextExecutor, Future}

/**
 * Created by gerardo.mendez on 7/27/16.
 */
trait Service extends Protocols { this: Core =>


  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: ActorMaterializer

  private implicit val timeout: Timeout = Timeout(10.seconds)

  val routes = {

    def extractLimit = parameters("limit".as[Int] ? 20, "after".as[Long] ? , "before".as[Long] ?)

    def assets = getFromDirectory("src/main/resources/web") ~ pathEndOrSingleSlash {
      get {
        redirect("index.html", PermanentRedirect)
      }
    }

    def api = pathPrefix("api" / "v1") {
      pathPrefix("videos") {
        get {
          pathEndOrSingleSlash {
            complete ((core ? GetPopularVideos()).asInstanceOf[Future[List[String]]])

          } ~ (path(Segment / "comments") & extractLimit).as(GetComments) { getComments =>
            complete ((core ? getComments).asInstanceOf[Future[List[Comment]]])

          }
        }
      }
    }

    api ~ assets
  }
}
