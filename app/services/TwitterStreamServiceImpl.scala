package services

import javax.xml.ws.http.HTTPException

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpHeader.ParsingResult
import akka.http.scaladsl.model.{ContentType, MediaTypes, _}
import akka.stream.ActorMaterializer
import com.hunorkovacs.koauth.domain.KoauthRequest
import com.hunorkovacs.koauth.service.consumer.DefaultConsumerService
import play.api.Play

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TwitterStreamServiceImpl extends TwitterStreamService {
  implicit lazy val actorSystem: ActorSystem = Play.unsafeApplication.injector.instanceOf[ActorSystem]

  //Get your credentials from https://apps.twitter.com and replace the values below
  private val consumerKey = "bgQEXVhm1HsTxF4Eq2eC3CuFd"
  private val consumerSecret = "akz50EktUmJQq4Q2U52BkHgMJwRt3rVZHbJ7K0aclBapbrSdJe"
  private val accessToken = "706874741957783560-ja2IiSQ0ZQ5PPsGroIPCjp8p4tzpwmx"
  private val accessTokenSecret = "g1LdmijUXAXgPhjqZEoVoOePXwUhECZXTDi3b6xAY6qoM"
  private val url = "https://stream.twitter.com/1.1/statuses/filter.json"

  implicit val materializer = ActorMaterializer()
  private val consumer = new DefaultConsumerService(actorSystem.dispatcher)

  def produceStream(trackWord: String) = {
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
        response.status.intValue() match {
          case 200 => response.entity.dataBytes
          case _ =>
            println(s"${response.status.intValue} ${response.status.reason}")
            throw new HTTPException(response.status.intValue())
        }
      }
    }
  }
}