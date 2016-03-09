package controllers

import akka.actor.ActorSystem
import javax.inject._

import actors.MyWebSocketActor
import play.api.mvc._
import play.api.Play.current

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` that demonstrates how to write
 * simple asychronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 *
 * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
 * run code after a delay.
 * @param exec We need an `ExecutionContext` to execute our
 * asynchronous code.
 */
@Singleton
class AsyncController @Inject() ()(implicit exec: ExecutionContext, actorSystem: ActorSystem) extends Controller {

  def socket = WebSocket.acceptWithActor[String, String] { request => out =>
    MyWebSocketActor.props(out)
  }

}
