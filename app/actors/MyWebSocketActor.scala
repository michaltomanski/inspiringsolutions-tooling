package actors

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.actor._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{MediaTypes, _}
import akka.http.scaladsl.model.HttpHeader.ParsingResult
import akka.pattern.{ask, pipe}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import core.Global
import models.Tweet

import scala.concurrent.ExecutionContext.Implicits.global
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
object MyWebSocketActor {
  def props(out: ActorRef) = Props(new MyWebSocketActor(out, Global.webSocketCoordinator))
}

class MyWebSocketActor(out: ActorRef, coordinator: ActorRef) extends Actor {

  protected implicit val akkaAskTimeout = Timeout(1000, TimeUnit.MILLISECONDS)


  def receive = {
    case msg: String =>
      out ! msg
  }

  override def preStart() {
    println("---------------- Registering socket actor")
    coordinator ! RegisterSocketActor("d", self)
  }


}
