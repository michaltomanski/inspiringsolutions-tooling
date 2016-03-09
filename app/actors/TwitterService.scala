package actors


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpHeader.ParsingResult
import akka.http.scaladsl.model.{ContentType, MediaTypes, _}
import akka.stream.ActorMaterializer
import com.google.inject.ImplementedBy
import com.hunorkovacs.koauth.service.consumer.DefaultConsumerService
import org.json4s.DefaultFormats
import com.hunorkovacs.koauth.domain.KoauthRequest
import models.Tweet
import org.json4s.native.JsonMethods._
import com.typesafe.config.ConfigFactory
import core.Global
import play.api.Play

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

@ImplementedBy(classOf[TwitterServiceImpl])
trait TwitterService

class TwitterServiceImpl extends TwitterService {
  implicit lazy val actorSystem: ActorSystem = Play.unsafeApplication.injector.instanceOf[ActorSystem]

  val conf = ConfigFactory.load()

  //Get your credentials from https://apps.twitter.com and replace the values below
  private val consumerKey = "bgQEXVhm1HsTxF4Eq2eC3CuFd"
  private val consumerSecret = "akz50EktUmJQq4Q2U52BkHgMJwRt3rVZHbJ7K0aclBapbrSdJe"
  private val accessToken = "706874741957783560-ja2IiSQ0ZQ5PPsGroIPCjp8p4tzpwmx"
  private val accessTokenSecret = "g1LdmijUXAXgPhjqZEoVoOePXwUhECZXTDi3b6xAY6qoM"
  private val url = "https://stream.twitter.com/1.1/statuses/filter.json"

  implicit val materializer = ActorMaterializer()
  implicit val formats = DefaultFormats

  private val consumer = new DefaultConsumerService(actorSystem.dispatcher)

  val body = "track=fuck"
  val source = Uri(url)

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

  oauthHeader.onComplete {
    case Success(header) =>
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
      request.flatMap { response =>
        if (response.status.intValue() != 200) {
          println(response.entity.dataBytes.runForeach(_.utf8String))
          Future(Unit)
        } else {
          response.entity.dataBytes
            .scan("")((acc, curr) => if (acc.contains("\r\n")) curr.utf8String else acc + curr.utf8String)
            .filter(_.contains("\r\n"))
            .map(json => Try(parse(json).extract[Tweet]))
            .runForeach {
              case Success(tweet) =>
                Global.webSocketCoordinator ! tweet.text
              case Failure(e) =>
                println("-----")
                println(e.getStackTrace)
            }
        }
      }
    case Failure(failure) => println(failure.getMessage)
  }

}