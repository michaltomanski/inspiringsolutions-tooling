import java.util.Random

import com.inspiringsolutions.tweet.actors.WebSocketActor
import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit, TestProbe}
import com.inspiringsolutions.tweet.models.{Entities, Tweet, Users}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import play.api.inject.guice.GuiceApplicationBuilder
import com.inspiringsolutions.tweet.services.TwitterStreamProducerService
import play.api.Application
import play.api.test.Helpers._
import play.api.inject.bind

import scala.concurrent.duration._

/**
  * Created by mtomanski on 10.03.16.
  */
class TwitterStreamSpec extends TestKit(ActorSystem("TwitterStreamSpec")) with DefaultTimeout with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  val random = new Random()

  "socket actor" must {
    "receive a message containing `java` and `scala`" in {
      val testTweet = generateTweet("Why use java, when you have scala?\r\n", "mtomanski")
      val application = generateAppWithTweets(testTweet)
      val socketRef = TestProbe()

      running(application) {
        val socketActor = system.actorOf(WebSocketActor.props(socketRef.ref, Option("scala")))

        val msg = socketRef.receiveOne(10.seconds).asInstanceOf[String]
        msg.toLowerCase.contains("java") should be(true)
        msg.toLowerCase.contains("scala") should be(true)
      }
    }

    "must not get message without given word" in {
      val testTweet = generateTweet("No text", "mtomanski")
      val application = generateAppWithTweets(testTweet)
      val socketRef = TestProbe()

      running(application) {
        val socketActor = system.actorOf(WebSocketActor.props(socketRef.ref, Option("scala")))

        val msgs = socketRef.receiveWhile(3.seconds) {
          case msg: Any => true
        }
        msgs.size should be(0)
      }
    }
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

  private def generateAppWithTweets(tweet: Tweet): Application = {
    new GuiceApplicationBuilder().overrides(bind[TwitterStreamProducerService].toInstance(new TwitterStreamProducerServiceMock(tweet))).build
  }
}
