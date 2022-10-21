package org.cscie88c.FinalProject

import akka.stream._
import akka.stream.scaladsl._
import akka.{ Done, NotUsed }
import akka.actor.ActorSystem
import akka.util.ByteString
import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths
import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl._
import org.scalacheck.{Arbitrary, Gen, ScalacheckShapeless}
import org.scalacheck.Prop.forAll

final case class Hashtag(name: String)

final case class Tweet(author: String, timestamp: Long, body: String) {
  def hashtags: Set[Hashtag] =
    body
      .split(" ")
      .collect {
        case t if t.startsWith("#") => Hashtag(t.replaceAll("[^#\\w]", ""))
      }
      .toSet
}

object Main extends App {
  val akkaTag = Hashtag("#akka")

  val tweets: Source[Tweet, NotUsed] = Source(
    Tweet("rolandkuhn", System.currentTimeMillis, "#akka rocks!") ::
      Tweet("rolandkuhn", System.currentTimeMillis, "#akka !") ::
      Tweet("bantonsson", System.currentTimeMillis, "#akka !") ::
      Tweet("drewhk", System.currentTimeMillis, "#akka !") ::
      Tweet("ktosopl", System.currentTimeMillis, "#akka on the rocks!") ::
      Tweet("mmartynas", System.currentTimeMillis, "wow #akka !") ::
      Tweet("akkateam", System.currentTimeMillis, "#akka rocks!") ::
      Tweet("bananaman", System.currentTimeMillis, "#bananas rock!") ::
      Tweet("appleman", System.currentTimeMillis, "#apples rock!") ::
      Tweet("drama", System.currentTimeMillis, "we compared #apples to #oranges!") ::
      Nil)

  implicit val system: ActorSystem = ActorSystem("reactive-tweets")

  tweets
    .filterNot(_.hashtags.contains(akkaTag)) // Remove all tweets containing #akka hashtag
    .map(_.hashtags) // Get all sets of hashtags ...
    .reduce(_ ++ _) // ... and reduce them to a single set, removing duplicates across all tweets
    .mapConcat(identity) // Flatten the set of hashtags to a stream of hashtags
    .map(_.name.toUpperCase) // Convert all hashtags to upper case
    .runWith(Sink.foreach(println)) // Attach the Flow to a Sink that will finally print the hashtags


}
