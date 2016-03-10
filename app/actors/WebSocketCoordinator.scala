package actors

import akka.actor._

/**
  * Created by mtomanski on 09.03.16.
  */
class WebSocketCoordinator extends Actor {

  var actorMap = Map[String, Seq[ActorRef]]()

  override def receive() = {
    case RegisterSocketActor(hashTag, ref) =>
      handleNewSocketActor(hashTag, ref)
    case msg: String =>
      forwardTwit(msg)
  }

  private def forwardTwit(msg: String) {
    actorMap.get(MyWebSocketActor.WordFilter).foreach{ refs =>
      refs.foreach { ref => ref ! msg}
    }
  }

  private def handleNewSocketActor(hashTag: String, ref: ActorRef) {
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
