package actors

import akka.actor._
import models.Tweet
import play.api.Play
import services.TwitterService

/**
  * Created by mtomanski on 09.03.16.
  */
class WebSocketCoordinatorActor extends Actor {

  var actorMap = Map[String, Seq[ActorRef]]()

  override def receive() = {
    case RegisterSocketActor(hashTag) =>
      handleNewSocketActor(hashTag, sender())
    case tweet: Tweet =>
      forwardTwit(tweet)
    case UnregisterSocketActor(hashTag) =>
      handleUnregisterSocketActor(hashTag, sender())
  }

  private def forwardTwit(tweet: Tweet) {
    actorMap.filter{case (tag, refs) => tweet.text.toLowerCase.contains(tag.toLowerCase)}.foreach{ case (tag, refs) =>
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
    println(s" --------- new socket actor registered for `$hashTag`")

  }

  private def handleUnregisterSocketActor(hashTag: String, ref: ActorRef): Unit = {
    val newRefSeq = actorMap.get(hashTag).map(_.filterNot(_ == ref)).getOrElse(Seq.empty)
    actorMap += (hashTag -> newRefSeq)
    println(s" --------- socket actor unregistered for #$hashTag")
  }

}


case class RegisterSocketActor(hashTag: String)

case class UnregisterSocketActor(hashTag: String)


object WebSocketCoordinatorActor {
  def props = Props(new WebSocketCoordinatorActor)
}