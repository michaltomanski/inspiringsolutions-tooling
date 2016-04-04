import java.util.Random

import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import play.api.inject.guice.GuiceApplicationBuilder
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

}
