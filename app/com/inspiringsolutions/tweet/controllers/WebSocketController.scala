package com.inspiringsolutions.tweet.controllers

import javax.inject._

import akka.actor.ActorSystem
import akka.util.Timeout
import com.inspiringsolutions.tweet.actors.WebSocketActor
import org.slf4j.LoggerFactory
import play.api.Play.current
import play.api.mvc._

import scala.concurrent.duration.DurationLong
import scala.concurrent.ExecutionContext

@Singleton
class WebSocketController @Inject() () (implicit exec: ExecutionContext, actorSystem: ActorSystem) extends Controller {

  private implicit val akkaAskTimeout = Timeout(10000L.millis)

  val log = LoggerFactory.getLogger(getClass)

  def socket(keyword: Option[String]) = WebSocket.acceptWithActor[String, String] { request => out =>
    WebSocketActor.props(out, keyword)
  }

}
