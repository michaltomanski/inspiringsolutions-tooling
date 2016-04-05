package com.inspiringsolutions.tweet.controllers

import javax.inject._

import akka.actor.{ActorSystem, Props}
import akka.stream.Materializer
import akka.util.Timeout
import com.inspiringsolutions.tweet.actors.{WebSocketActor, WebSocketCoordinatorActor}
import com.inspiringsolutions.tweet.services.TwitterService
import org.slf4j.LoggerFactory
import play.api.inject.guice.GuiceInjectorBuilder
import play.api.{Environment, Play}
import play.api.libs.streams.ActorFlow
import play.api.mvc._

import scala.concurrent.duration.DurationLong
import scala.concurrent.ExecutionContext

@Singleton
class WebSocketController @Inject() () (implicit exec: ExecutionContext, actorSystem: ActorSystem, materializer: Materializer) extends Controller {

  private implicit val akkaAskTimeout = Timeout(10000L.millis)

  val log = LoggerFactory.getLogger(getClass)

  val coordinatorActor = actorSystem.actorOf(WebSocketCoordinatorActor.props)

  def socket(keyword: Option[String]) = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => WebSocketActor.props(out, coordinatorActor, keyword))
  }

}
