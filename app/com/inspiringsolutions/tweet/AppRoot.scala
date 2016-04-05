package com.inspiringsolutions.tweet

import javax.inject.Inject

import akka.actor.ActorSystem
import com.inspiringsolutions.tweet.actors.WebSocketCoordinatorActor
import com.inspiringsolutions.tweet.core.{GlobalInjector, Injection}
import play.api.Application

/**
  * Created by mtomanski on 06.04.16.
  */

object AppRoot extends Injection  {
  lazy val akka = inject[ActorSystem]

  lazy val userSocketCoordinatorActor = akka.actorOf(WebSocketCoordinatorActor.props)
}

class AppRoot @Inject() (app: Application) {

  GlobalInjector.app = app

}