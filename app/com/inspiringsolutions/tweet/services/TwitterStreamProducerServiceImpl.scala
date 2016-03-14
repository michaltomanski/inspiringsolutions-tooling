package com.inspiringsolutions.tweet.services

import javax.xml.ws.http.HTTPException

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpHeader.ParsingResult
import akka.http.scaladsl.model.{ContentType, MediaTypes, _}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.hunorkovacs.koauth.domain.KoauthRequest
import com.hunorkovacs.koauth.service.consumer.DefaultConsumerService
import com.typesafe.config.ConfigFactory
import play.api.Play

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TwitterStreamProducerServiceImpl extends TwitterStreamProducerService {
  implicit lazy val actorSystem: ActorSystem = Play.unsafeApplication.injector.instanceOf[ActorSystem]

  //Get your credentials from https://apps.twitter.com and replace the values below
  private val consumerKey = ConfigFactory.load().getString("twitter.config.consumer.key")
  private val consumerSecret = ConfigFactory.load().getString("twitter.config.consumer.secret")
  private val accessToken = ConfigFactory.load().getString("twitter.config.access.token.key")
  private val accessTokenSecret = ConfigFactory.load().getString("twitter.config.access.token.secret")
  private val url = "https://stream.twitter.com/1.1/statuses/filter.json"

  implicit val materializer = ActorMaterializer()
  private val consumer = new DefaultConsumerService(actorSystem.dispatcher)

  def produceStream(trackWord: String): Future[Source[ByteString, Any]] = {
    val source = Uri(url)
    val body = s"track=$trackWord"

    //Create Oauth 1a header
    val oauthHeader: Future[String] = consumer.createOauthenticatedRequest(
      KoauthRequest(
        method = "POST",
        url = url,
        authorizationHeader = None,
        body = Some(body)
      ),
      consumerKey,
      consumerSecret,
      accessToken,
      accessTokenSecret
    ) map (_.header)

    oauthHeader.flatMap { header =>
      val httpHeaders: List[HttpHeader] = List(
        HttpHeader.parse("Authorization", header) match {
          case ParsingResult.Ok(h, _) => Some(h)
          case _ => None
        },
        HttpHeader.parse("Accept", "*/*") match {
          case ParsingResult.Ok(h, _) => Some(h)
          case _ => None
        }
      ).flatten

      val httpRequest: HttpRequest = HttpRequest(
        method = HttpMethods.POST,
        uri = source,
        headers = httpHeaders,
        entity = HttpEntity(contentType = ContentType(MediaTypes.`application/x-www-form-urlencoded`, HttpCharsets.`UTF-8`), string = body)
      )

      val request = Http().singleRequest(httpRequest)
      request.map { response =>
        response.status.intValue match {
          case 200 => response.entity.dataBytes
          case 420 => throw new IllegalStateException("Filter to wide...")
          case _ =>
            println(s"${response.status.intValue} ${response.status.reason}")
            throw new HTTPException(response.status.intValue())
        }
      }
    }
  }
}