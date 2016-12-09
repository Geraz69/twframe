package com.twframe

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.twframe.Entities._
import spray.json._
/**
 * Created by gerardo.mendez on 7/27/16.
 */

trait Protocols extends SprayJsonSupport with DefaultJsonProtocol {
  implicit object statusFormat extends RootJsonFormat[Comment] {
    def read(value: JsValue) = ???
    def write(c: Comment) = {
      JsObject(
        "id" -> JsString(c.id),
        "video" -> JsString(c.video),
        "createdAt" -> JsNumber(c.id),
        "time" -> c.time.map(JsNumber(_)).getOrElse(JsNull),
        "inReplyTo" -> c.inReplyTo.map(JsString(_)).getOrElse(JsNull)
      )
    }
  }
}