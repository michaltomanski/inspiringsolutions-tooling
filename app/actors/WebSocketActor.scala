package actors

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.util.Timeout
import core.Global
import models.Tweet
import play.api.Play
import services.TwitterProcessor

object WebSocketActor {
  def props(out: ActorRef) = Props(new WebSocketActor(out, Global.webSocketCoordinator))
  val WordFilter = "java"
}

class WebSocketActor(out: ActorRef, coordinator: ActorRef) extends Actor {
  lazy val twitterProcessor = Play.unsafeApplication.injector.instanceOf[TwitterProcessor]

  protected implicit val akkaAskTimeout = Timeout(1000, TimeUnit.MILLISECONDS)

  def receive = {
    case tweet: Tweet =>
      out ! twitterProcessor.processTweet(tweet)
  }

  override def preStart() {
    // For now we register all sockets to listen for the same word appearing in tweet
    coordinator ! RegisterSocketActor(WebSocketActor.WordFilter)
  }

  override def postStop() {
    coordinator ! UnregisterSocketActor(WebSocketActor.WordFilter)
  }

}
