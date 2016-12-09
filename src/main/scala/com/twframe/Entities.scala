package com.twframe

/**
 * Created by gerardo.mendez on 7/31/16.
 */
object Entities {

  case class GetVideos(offset: Int = 0, limit: Int = 20)
  case class GetPopularVideos(offset: Int = 0, limit: Int = 20)

  case class GetComments(video: String, limit: Int, after: Option[Long] = None, before: Option[Long] = None)
  case class GetPopularComments(video: String, limit: Option[Int] = None)

  case class SearchComments(query: String)
  case class StreamComments(query: String)

  case class Comment(id: String, video: String,
                     createdAt: Long,
                     time: Option[Integer] = None,
                     inReplyTo: Option[String] = None)
}
