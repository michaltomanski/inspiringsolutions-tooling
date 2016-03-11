import javax.inject.Inject

import actors.{WebSocketActor, WebSocketCoordinator}
import akka.actor.ActorSystem
import akka.stream.scaladsl.Source
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import play.api.Play
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeApplication
import services.{TwitterService, TwitterStreamService}
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

  "socket uctor" must {
    "receive a message" in {
      running(application) {
        //val tw = Play.unsafeApplication.injector.instanceOf[TwitterService]
        //tw.processStreamToActorRef(self)
        val socketActor = system.actorOf(WebSocketActor.props(socketRef.ref))
        val x = 1+1
        socketRef.expectMsg(2500.millis)
        true should be(true)
      }
    }
  }



}
