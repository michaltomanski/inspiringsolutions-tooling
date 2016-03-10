package actors

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.util.Timeout
import core.Global
object MyWebSocketActor {
  def props(out: ActorRef) = Props(new MyWebSocketActor(out, Global.webSocketCoordinator))
  val WordFilter = "java"
}

class MyWebSocketActor(out: ActorRef, coordinator: ActorRef) extends Actor {

  protected implicit val akkaAskTimeout = Timeout(1000, TimeUnit.MILLISECONDS)

  def receive = {
    case msg: String =>
      out ! msg
  }

  override def preStart() {
    println("---------------- Registering new socket actor ----------------")
    // For now we register all sockets to listen for everything that
    coordinator ! RegisterSocketActor(MyWebSocketActor.WordFilter, self)
  }


}
