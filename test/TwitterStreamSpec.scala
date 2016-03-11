import javax.inject.Inject

import actors.{WebSocketActor, WebSocketCoordinator}
import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import play.api.inject.guice.GuiceApplicationBuilder
import services.TwitterStreamService
import play.api.test.Helpers._
import play.api.inject.bind

import scala.concurrent.duration._

/**
  * Created by mtomanski on 10.03.16.
  */
class TwitterStreamSpec extends TestKit(ActorSystem("TwitterStreamSpec")) with DefaultTimeout with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  val socketRef = TestProbe()

  val application = new GuiceApplicationBuilder()
    .overrides(bind[TwitterStreamService].to[TwitterStreamServiceMock])
    .build

  "socket actor" must {
    "receive a message containing `java`" in {
      running(application) {
        val socketActor = system.actorOf(WebSocketActor.props(socketRef.ref))

        val msg = socketRef.receiveOne(2.seconds).asInstanceOf[String]
        msg.toLowerCase.contains("java") should be(true)
      }
    }

    "receive a message processed correctly" in {
      running(application) {
        val msg = socketRef.receiveOne(2.seconds).asInstanceOf[String]
        val lines = msg.split("::::").map(_.trim)

        lines(1).contains("Length:") should be(true)
        lines(2).reverse should be(lines(3))
      }
    }
  }



}
