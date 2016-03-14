package com.inspiringsolutions.tweet.core

import akka.actor.ActorSystem
import com.inspiringsolutions.tweet.actors.WebSocketCoordinatorActor
import org.slf4j.LoggerFactory
import play.api.{GlobalSettings, Play}

object Global extends GlobalSettings {
  private val log = LoggerFactory.getLogger(getClass)

  lazy val actorSystem = Play.unsafeApplication.injector.instanceOf[ActorSystem]

  lazy val webSocketCoordinator = actorSystem.actorOf(WebSocketCoordinatorActor.props)

  override def onStart(app: play.api.Application): Unit = {
    log.info("App startup...")
  }

}