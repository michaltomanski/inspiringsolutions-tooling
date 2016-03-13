package actors

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.util.Timeout
import core.Global
import models.Tweet
import play.api.Play
import services.TwitterProcessor

object WebSocketActor {
  def props(out: ActorRef, tag: String) = Props(new WebSocketActor(out, Global.webSocketCoordinator, tag))
  val WordFilter = "inspiringsolutions"
}

class WebSocketActor (out: ActorRef, coordinator: ActorRef, tag: String) extends Actor {
  lazy val twitterProcessor = Play.unsafeApplication.injector.instanceOf[TwitterProcessor]
  protected implicit val akkaAskTimeout = Timeout(1000, TimeUnit.MILLISECONDS)

  def receive = {
    case tweet: Tweet =>
      out ! twitterProcessor.processTweet(tweet)
  }

  override def preStart() {
    coordinator ! RegisterSocketActor(tag)
  }

  override def postStop() {
    coordinator ! UnregisterSocketActor(tag)
  }

}

