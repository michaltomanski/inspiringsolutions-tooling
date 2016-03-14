package com.inspiringsolutions.tweet.actors

import akka.actor._
import com.inspiringsolutions.tweet.core.Global
import com.inspiringsolutions.tweet.models.{SimplifiedTweet, Tweet}
import play.api.libs.json.Json

object WebSocketActor {
  def props(out: ActorRef, keyword: Option[String]) = Props(new WebSocketActor(out, Global.webSocketCoordinator, keyword))
}

class WebSocketActor (out: ActorRef, coordinator: ActorRef, keyword: Option[String]) extends Actor {

  def receive = {
    case tweet: Tweet =>
      out ! Json.toJson(SimplifiedTweet(tweet.id_str, tweet.text, tweet.user.name, tweet.user.profile_image_url)).toString()
  }

  override def preStart() {
    coordinator ! RegisterSocketActor(keyword)
  }

  override def postStop() {
    coordinator ! UnregisterSocketActor
  }

}

