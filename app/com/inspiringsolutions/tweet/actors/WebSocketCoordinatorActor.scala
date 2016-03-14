package com.inspiringsolutions.tweet.actors

import akka.actor.Status.{Success,Failure}
import akka.actor._
import com.inspiringsolutions.tweet.models.Tweet
import com.inspiringsolutions.tweet.services.TwitterService
import org.slf4j.LoggerFactory
import play.api.Play

/**
  * Created by mtomanski on 09.03.16.
  */
class WebSocketCoordinatorActor(twitterService: TwitterService) extends Actor {

  private val log = LoggerFactory.getLogger(getClass)

  private var twitterConsumerActor: Option[ActorRef] = None

  private var keywordMap = Map[Option[String], Seq[ActorRef]]()
  private var reverseKeywordMap = Map[ActorRef, Option[String]]()

  private implicit val ec = context.dispatcher

  override def receive() = {
    case RegisterSocketActor(keyword) =>
      handleNewSocketActor(sender, keyword)
    case tweet: Tweet =>
      forwardTweet(tweet)
    case UnregisterSocketActor =>
      handleUnregisterSocketActor(sender)
    case RestartStreaming(hashTag: String) =>
      try {
        handleRestart(hashTag)
        sender ! Success
      } catch {
        case e: Exception => sender ! Failure(e)
      }
    case StopStreaming =>
      handleStop
  }

  private def handleRestart(hashTag: String) {
    if(twitterConsumerActor.isDefined) {
      handleStop
    }

    val consumerActorRef = context.actorOf(TwitterConsumerActor.props(self))
    twitterService.processStreamToActorRef(consumerActorRef, hashTag)
    twitterConsumerActor = Option(consumerActorRef)

    log.info(s"Streaming started to text: ${hashTag}...")
  }

  private def handleStop {
    twitterConsumerActor.foreach { actor =>
      actor ! PoisonPill
    }

    twitterConsumerActor = None

    log.info("Stream has been killed...")
  }

  private def forwardTweet(tweet: Tweet) {
    log.debug(s"Tweet to be forwared: ${tweet.id}")

    for {
      sequences <- keywordMap.filterKeys(key => tweet.text.toLowerCase.contains(key.getOrElse("").toLowerCase)).values
      actor <- sequences
    } {
      actor ! tweet
    }
  }

  private def handleNewSocketActor(ref: ActorRef, keyword: Option[String]) {
    if (twitterConsumerActor.isEmpty) {
      handleRestart("inspiringsolutions")
    }

    val newRefSeq = keywordMap.get(keyword)
      .map { refSeq => ref +: refSeq }
      .getOrElse(Seq(ref))

    keywordMap += (keyword -> newRefSeq)

    reverseKeywordMap += (ref -> keyword)

    log.info(s"New socket actor registered with filter: ${keyword}")

  }

  private def handleUnregisterSocketActor(ref: ActorRef) {
    val maybeKeyword = reverseKeywordMap.get(ref)

    for {
      keyword <- maybeKeyword
      actorSeq <- keywordMap.get(keyword)
    } {
      keywordMap += (keyword -> actorSeq.filter(_ != ref))
      reverseKeywordMap = reverseKeywordMap.filterKeys(_ != ref)
    }

    log.info(s"Socket actor unregistered")
  }
}


case class RegisterSocketActor(keyword: Option[String])

case object UnregisterSocketActor

case class RestartStreaming(hashTag: String)

case object StopStreaming

object WebSocketCoordinatorActor {
  def props = Props(new WebSocketCoordinatorActor(Play.unsafeApplication.injector.instanceOf[TwitterService]))
}