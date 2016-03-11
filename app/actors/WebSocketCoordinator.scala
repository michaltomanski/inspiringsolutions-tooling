package actors

import akka.actor._
import models.Tweet
import play.api.Play
import services.TwitterService

/**
  * Created by mtomanski on 09.03.16.
  */
class WebSocketCoordinator extends Actor {

  var actorMap = Map[String, Seq[ActorRef]]()

  override def receive() = {
    case RegisterSocketActor(hashTag, ref) =>
      handleNewSocketActor(hashTag, ref)
    case tweet: Tweet =>
      forwardTwit(tweet)
  }

  private def forwardTwit(tweet: Tweet) {
    actorMap.get(WebSocketActor.WordFilter).foreach{ refs =>
      refs.foreach { ref => ref ! tweet}
    }
  }

  private def handleNewSocketActor(hashTag: String, ref: ActorRef) {
    if (actorMap.isEmpty) {
      println(" ---------- starting Twitter Service ------ ")
      val tw = Play.unsafeApplication.injector.instanceOf[TwitterService]
      tw.processStreamToActorRef(self)
    }

    val newRefSeq = actorMap.get(hashTag)
      .map { refSeq => ref +: refSeq }
      .getOrElse(Seq(ref))

    actorMap += (hashTag -> newRefSeq)
    println(s" --------- new socket actor registered for #$hashTag")

  }

}

object WebSocketCoordinator {
  def props = Props(new WebSocketCoordinator)
}

case class RegisterSocketActor(hashTag: String, ref: ActorRef)
