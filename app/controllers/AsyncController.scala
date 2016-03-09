package controllers

import akka.actor.ActorSystem
import javax.inject._

import actors.{MyWebSocketActor, TwitterActor}
import akka.stream.ActorMaterializer
import play.api._
import play.api.mvc.WebSocket._
import play.api.mvc._
import play.api.Play.current

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.duration._

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

  implicit val materializer = ActorMaterializer()

  def socket = WebSocket.acceptWithActor[String, String] { request => out =>
    MyWebSocketActor.props(out)
  }

  /**
   * Create an Action that returns a plain text message after a delay
   * of 1 second.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/message`.
   */
  def message = Action {
    val twitterer = TwitterActor
    println("ACTOR INITIALISED")
    Ok
  }

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) { promise.success("Hi!") }
    promise.future
  }

}
