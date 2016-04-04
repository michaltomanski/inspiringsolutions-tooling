import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import play.api.test.Helpers._
import actors.{CounterActor, Message}
import akka.pattern.ask

import scala.util.Success

/**
  * Created by mtomanski on 10.03.16.
  */
class CounterActorSpec extends TestKit(ActorSystem("CounterActorSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  "CounterActor" must {
    "Return `got it` message when a message is sent" in {
      val counterActor = TestActorRef[CounterActor]
      val messageValue = "dd"
      val future = counterActor ? Message(messageValue)

      val Success(result: String) = future.value.get
      result should be (messageValue)
    }
  }

}
