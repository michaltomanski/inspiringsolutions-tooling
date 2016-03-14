package com.inspiringsolutions.tweet.actors

import akka.actor.{Actor, ActorRef, Props}
import com.inspiringsolutions.tweet.models.{LimitNotice, Tweet}
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

/**
  * Created by pdolega on 3/13/16.
  */
class TwitterConsumerActor(coordinatorRef: ActorRef) extends Actor {

  private val log = LoggerFactory.getLogger(getClass)

  override def receive: Receive = {

    case next: Try[Any] =>
      processParseResult(next)
  }

  private def processParseResult(result: Try[Any]) {
    result match {
      case Success(tweet: Tweet) =>
        log.debug(s"-----\n${tweet.text}")
        coordinatorRef ! tweet
      case Success(limit: LimitNotice) =>
        log.warn("Exceeded tweet velocity!")
      case Failure(e) =>
        log.error("Error while parsing Tweet", e)
      case unknown: Any =>
        log.warn(s"Unknown result: ${unknown}")
    }
  }
}

object TwitterConsumerActor {
  def props(coordinatorRef: ActorRef) = Props(classOf[TwitterConsumerActor], coordinatorRef)
}

case object CompleteStream