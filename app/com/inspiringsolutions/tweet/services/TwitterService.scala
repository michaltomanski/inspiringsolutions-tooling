package com.inspiringsolutions.tweet.services

import javax.inject.{Inject, Singleton}

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.hunorkovacs.koauth.service.consumer.DefaultConsumerService
import com.inspiringsolutions.tweet.actors.CompleteStream
import com.inspiringsolutions.tweet.models.{LimitNotice, Tweet}
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

@Singleton
class TwitterService @Inject() (twitterStreamService: TwitterStreamProducerService)  {

  private val log = LoggerFactory.getLogger(getClass)

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  implicit val formats = DefaultFormats

  def processStreamToActorRef(streamConsumerRef: ActorRef, hashTag: String) {
    val streamFuture = twitterStreamService.produceStream(hashTag)

    streamFuture.onComplete {
      case Failure(ex) => log.error("Error while processing stream", ex)
      case Success(stream) =>
        stream.scan("")((acc, curr) => {
          if (acc.contains("\r\n"))
            curr.utf8String
          else
            acc + curr.utf8String
        })
        .filter(_.contains("\r\n")).filterNot(_.trim.isEmpty)
        .map { tryParse }
        .runWith(Sink.actorRef(streamConsumerRef, CompleteStream))
    }
  }

  private def tryParse(json: String) = {
    val parsed = parse(json)
    val tweetAttempt = Try(parsed.extract[Tweet])
    if (tweetAttempt.isSuccess) {
      tweetAttempt
    } else {
      Try(parsed.extract[LimitNotice])
    }
  }
}