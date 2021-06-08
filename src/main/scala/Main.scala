import java.net.URLEncoder
import java.time.LocalDate

import io.circe.Encoder
import io.circe.Json
import io.circe.JsonObject
import io.circe.syntax._
import java.util.Base64
import java.nio.charset.StandardCharsets

/**
  * Construct a Facebook post search query string, searching your groups, for posts created
  * within an arbitrary number of days.
  *
  * Usage: [+days] phrase
  * Sbt usage: sbt --error "run [+days] phrase"
  *
  * By default days is 0, returning only posts created today.
  *
  */
object Main extends App {
  val utf8 = StandardCharsets.UTF_8

  val (days: Int, phrase: String) = args.toList match {
    case x :: xs if x.forall(_.isDigit) => (x.toInt, URLEncoder.encode(xs.mkString(" "), utf8))
    case xs                             => (0, URLEncoder.encode(xs.mkString(" "), utf8))
    case _                              => throw new IllegalArgumentException(s"Usage: [+days] phrase")
  }

  val item: Json = Seq(
    JsonTypes.CreationTimeFilter(LocalDate.now().minusDays(days), LocalDate.now()).asJson,
    JsonTypes.AuthorFilter().asJson,
  ).fold(JsonObject.empty.asJson)(_ deepMerge _)

  val encoded = URLEncoder.encode(
    Base64.getEncoder.encodeToString(item.noSpaces.getBytes(utf8)),
    utf8,
  )

  println(s"https://www.facebook.com/search/posts?q=$phrase&filters=$encoded")
}

/**
  * Sample Facebook-generated JSON with all filters active:
  * {{{
  * {
  * "rp_author:0": "{\"name\":\"my_groups_and_pages_posts\",\"args\":\"\"}",
  * "rp_creation_time:0": "{\"name\":\"creation_time\",
  *    \"args\":\"{\\\"start_year\\\":\\\"2021\\\",\\\"start_month\\\":\\\"2021-1\\\",
  *    \\\"end_year\\\":\\\"2021\\\",\\\"end_month\\\":\\\"2021-12\\\",
  *    \\\"start_day\\\":\\\"2021-1-1\\\",\\\"end_day\\\":\\\"2021-12-31\\\"}\"}",
  * "recent_posts:0": "{\"name\":\"recent_posts\",\"args\":\"\"}",
  * "seen_posts:0": "{\"name\":\"interacted_posts\",\"args\":\"\"}",
  * "rp_location:0": "{\"name\":\"location\",\"args\":\"110398488982884\"}"
  * }
  * }}}
  */
object JsonTypes {

  case class CreationTimeFilter(
    start: LocalDate,
    end: LocalDate,
  )

  object CreationTimeFilter {
    implicit val encoder: Encoder[CreationTimeFilter] = (a: CreationTimeFilter) => {
      Json.obj(
        "rp_creation_time:0" ->
          Json
            .obj(
              "name" -> "creation_time".asJson,
              "args" -> Json
                .obj(
                  "start_year" -> a.start.getYear.toString.asJson,
                  "start_month" -> a.start.toString.take(7).asJson,
                  "start_day" -> a.start.toString.asJson,
                  "end_year" -> a.end.getYear.toString.asJson,
                  "end_month" -> a.end.toString.take(7).asJson,
                  "end_day" -> a.end.toString.asJson,
                )
                .noSpaces
                .asJson,
            )
            .noSpaces
            .asJson,
      )
    }
  }

  case class AuthorFilter()

  object AuthorFilter {
    implicit val encoder: Encoder[AuthorFilter] = (a: AuthorFilter) => {
      Json.obj(
        "rp_author:0" ->
          Json
            .obj(
              "name" -> "my_groups_and_pages_posts".asJson,
              "args" -> "".asJson,
            )
            .noSpaces
            .asJson,
      )
    }
  }
}
