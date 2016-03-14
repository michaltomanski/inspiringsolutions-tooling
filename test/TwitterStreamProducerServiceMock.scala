import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.inspiringsolutions.tweet.models.Tweet
import com.inspiringsolutions.tweet.services.TwitterStreamProducerService
import play.libs.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Created by mtomanski on 09.03.16.
  */
class TwitterStreamProducerServiceMock(tweet: Tweet) extends TwitterStreamProducerService {

  override def produceStream(trackWord: String): Future[Source[ByteString, Any]] = {
    val jsonTweet = Json.toJson(tweet).toString() + "\r\n"
    Future( Source.tick(0.seconds, 200.millisecond, toByteString(jsonTweet)) )
  }

  private def toByteString(obj: String): ByteString = {
    ByteString(obj)
  }


}
