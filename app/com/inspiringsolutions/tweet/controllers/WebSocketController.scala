package com.inspiringsolutions.tweet.controllers

import javax.inject._

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.inspiringsolutions.tweet.actors.{RestartStreaming, StopStreaming, WebSocketActor}
import com.inspiringsolutions.tweet.core.Global
import org.slf4j.LoggerFactory
import play.api.Play.current
import play.api.mvc._

import scala.concurrent.duration.DurationLong
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class WebSocketController @Inject() () (implicit exec: ExecutionContext, actorSystem: ActorSystem) extends Controller {

  private implicit val akkaAskTimeout = Timeout(10000L.millis)

  val log = LoggerFactory.getLogger(getClass)

  def socket(keyword: Option[String]) = WebSocket.acceptWithActor[String, String] { request => out =>
    WebSocketActor.props(out, keyword)
  }

  def stopStreaming = Action.async {
    Global.webSocketCoordinator ! StopStreaming
    Future(Ok)
  }

  def startStraming = Action.async {
    Global.webSocketCoordinator ? RestartStreaming map { _ =>
      Ok
    } recover {
      case e: IllegalStateException =>
        Status(420)("Filter too wide... Twitter rejected")
      case e: Exception =>
        log.error(s"Unable to listen on new hashTag", e)
        InternalServerError
    }
  }
}
