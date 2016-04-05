import java.util.Random

import com.inspiringsolutions.tweet.actors.{WebSocketActor, WebSocketCoordinatorActor}
import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit, TestProbe}
import com.inspiringsolutions.tweet.models.{Entities, Tweet, Users}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpecLike}
import play.api.inject.guice.GuiceApplicationBuilder
import com.inspiringsolutions.tweet.services.TwitterStreamProducerService
import org.specs2.specification.AfterEach
import play.api.Application
import play.api.test.Helpers._
import play.api.inject.bind

import scala.concurrent.duration._

/**
  * Created by mtomanski on 10.03.16.
  */
class TwitterStreamSpec extends TestKit(ActorSystem("TwitterStreamSpec")) with DefaultTimeout with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterEach {



  "socket actor" must {
    "receive a message containing `java` and `scala`" in {
      val testTweet = TestHelper.generateTweet("Why use java, when you have scala?\r\n", "mtomanski")
      val application = TestHelper.generateAppWithTweets(testTweet)
      val socketRef = TestProbe()

      running(application) {
        val coordinator = system.actorOf(WebSocketCoordinatorActor.props)
        val socketActor = system.actorOf(WebSocketActor.props(socketRef.ref, coordinator, Option("scala")))

        val msg = socketRef.receiveOne(10.seconds).asInstanceOf[String]
        msg.toLowerCase.contains("java") should be(true)
        msg.toLowerCase.contains("scala") should be(true)
      }
    }

    "must not get message without given word" in {
      val testTweet = TestHelper.generateTweet("Why use java, when you have scala?\r\n", "mtomanski")
      val application = TestHelper.generateAppWithTweets(testTweet)
      val socketRef = TestProbe()

      running(application) {
        val coordinator = system.actorOf(WebSocketCoordinatorActor.props)
        val socketActor = system.actorOf(WebSocketActor.props(socketRef.ref, coordinator, Option("php")))

        val msgs = socketRef.receiveWhile(3.seconds) {
          case msg: Any => true
        }
        msgs.size should be(0)
      }
    }
  }

}
