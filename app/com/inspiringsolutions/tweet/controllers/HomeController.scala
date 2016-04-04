package com.inspiringsolutions.tweet.controllers

import javax.inject.{Inject, Singleton}

import actors.{CounterActor, GetCounter, Message}
import akka.actor.{ActorSystem, Props}
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import akka.pattern.ask

import scala.concurrent.duration._

@Singleton
class HomeController @Inject() (actorSystem: ActorSystem) extends Controller {

  val counterActor = actorSystem.actorOf(Props(classOf[CounterActor]))

  def hello(msg: String) = Action.async {
    Future {
      counterActor ! Message(msg)
      Ok(s"Got $msg")
    }
  }

  def getCounter = Action.async {
    (counterActor ? GetCounter)(1.seconds).map {
      case result: Int =>
        Ok(result.toString)
    }
  }

}
