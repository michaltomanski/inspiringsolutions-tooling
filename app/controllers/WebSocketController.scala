package controllers

import akka.actor.ActorSystem
import javax.inject._

import actors.WebSocketActor
import play.api.mvc._
import play.api.Play.current

import scala.concurrent.ExecutionContext

@Singleton
class WebSocketController @Inject() () (implicit exec: ExecutionContext, actorSystem: ActorSystem) extends Controller {

  def socket(tag: String) = WebSocket.acceptWithActor[String, String] { request => out =>
    WebSocketActor.props(out, tag)
  }

}
