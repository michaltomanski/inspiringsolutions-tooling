package services

import javax.inject.Inject

import org.json4s.native.JsonMethods._
import actors.WebSocketActor
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import core.Global
import models.Tweet
import org.json4s.DefaultFormats
import play.api.Play

import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

class TwitterService @Inject() (twitterStreamService: TwitterStreamService)  {
  implicit lazy val actorSystem: ActorSystem = Play.unsafeApplication.injector.instanceOf[ActorSystem]
  implicit val materializer = ActorMaterializer()

  implicit val formats = DefaultFormats

  val stream = twitterStreamService.produceStream(WebSocketActor.WordFilter)

  stream.map(_.scan("")((acc, curr) => if (acc.contains("\r\n")) curr.utf8String else acc + curr.utf8String)
    .filter(_.contains("\r\n"))
    .map(json => Try(parse(json).extract[Tweet]))
    .runForeach {
      case Success(tweet) =>
        println("-----")
        println(tweet.text)
        Global.webSocketCoordinator ! tweet
      case Failure(e) =>
        println("-----")
        println(e.getStackTrace)
    }
  )

}