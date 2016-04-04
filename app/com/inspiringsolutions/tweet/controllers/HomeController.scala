package com.inspiringsolutions.tweet.controllers

import javax.inject.{Inject, Singleton}

import actors.{CounterActor, GetCounter, Message}
import akka.actor.{ActorSystem, Props}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._

@Singleton
class HomeController @Inject() (actorSystem: ActorSystem) extends Controller {

  val counterActor = actorSystem.actorOf(Props(classOf[CounterActor]))

  implicit val timeout = Timeout(1.seconds)

  def hello(msg: String) = Action.async {
      (counterActor ? Message(msg)).map {
        case result: String =>
          Ok(s"Got $result")
    }
  }

  def getCounter = Action.async {
    (counterActor ? GetCounter)(1.seconds).map {
      case result: Int =>
        Ok(result.toString)
    }
  }

}
