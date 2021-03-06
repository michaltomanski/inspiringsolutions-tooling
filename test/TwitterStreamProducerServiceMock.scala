import java.util.Random
import javax.inject.Inject

import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.inspiringsolutions.tweet.models.{Entities, Tweet, Users}
import com.inspiringsolutions.tweet.services.TwitterStreamProducerService
import play.api.Configuration
import play.libs.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Created by mtomanski on 09.03.16.
  */
class TwitterStreamProducerServiceMock (tweet: Tweet) extends TwitterStreamProducerService {

  val random = new Random()

  override def produceStream(trackWord: String): Future[Source[ByteString, Any]] = {
    val jsonTweet = Json.toJson(tweet).toString() + "\r\n"
    Future( Source.tick(0.seconds, 500.millisecond, toByteString(jsonTweet)) )
  }

  private def toByteString(obj: String): ByteString = {
    ByteString(obj)
  }

  private def generateTweet(text: String, name: String)= {
    val id = random.nextLong()
    Tweet(
      id = id,
      id_str = s"${id}",
      text = text,
      source = "\\u003ca href=\\\"http:\\/\\/twitter.com\\/download\\/iphone\\ rel=\\\"nofollow\\\"\\u003eTwitter for iPhone\\u003c\\/a\\u003e",
      truncated = false,
      user = Users(
        default_profile = true,
        entities = Entities(),
        id = id,
        id_str = s"${id}",
        default_profile_image = true,
        lang = "en",
        name = name,
        screen_name = name,
        verified = true
      ),
      entities = Entities()
    )
  }



}
