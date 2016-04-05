import com.inspiringsolutions.tweet.controllers.{HomeController, WebSocketController}
import com.inspiringsolutions.tweet.services.TwitterStreamProducerService
import org.specs2.mutable._
import play.api.inject._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test._
import play.api.test.Helpers._
import play.test.WithApplication
import play.api.test.FakeApplication


/**
  * Created by mtomanski on 05.04.16.
  */

class WebSocketControllerSpec extends Specification {

  "The 'Hello world' string" should {

    "contain 11 characters" in {
      val testTweet = TestHelper.generateTweet("No text", "mtomanski")
      val application = TestHelper.generateAppWithTweets(testTweet)
      running (application) {
        val controller = application.injector.instanceOf(classOf[HomeController])
        val result = controller.index()(FakeRequest())
        status(result) must equalTo(OK)
        //private val injector = application.injector
        //val controller = injector.instanceOf(classOf[WebSocketController])
        "Hello world" must have size (11)
      }
    }

    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }

    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }

}