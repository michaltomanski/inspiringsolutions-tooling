import javax.inject.Inject

import actors.WebSocketActor
import akka.actor.ActorSystem
import akka.stream.scaladsl.Source
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import services.TwitterStreamService

/**
  * Created by mtomanski on 10.03.16.
  */
class TwitterStreamSpec @Inject() (t) extends TestKit(ActorSystem("TwitterStreamSpec")) with DefaultTimeout with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  val probe = TestProbe()



  system.actorOf(WebSocketActor.props(probe.ref))



}
