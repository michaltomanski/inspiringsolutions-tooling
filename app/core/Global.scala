package core

import actors.{TwitterService, WebSocketCoordinator}
import akka.actor.ActorSystem
import play.api.{GlobalSettings, Play}

object Global extends GlobalSettings {
  lazy val actorSystem = Play.unsafeApplication.injector.instanceOf[ActorSystem]

  lazy val webSocketCoordinator = actorSystem.actorOf(WebSocketCoordinator.props)

  override def onStart(app: play.api.Application): Unit = {
    println("startup")

    val tw = Play.unsafeApplication.injector.instanceOf[TwitterService]
  }

}