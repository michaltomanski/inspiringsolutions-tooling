import java.util.Random

import com.inspiringsolutions.tweet.models.{Entities, Tweet, Users}
import com.inspiringsolutions.tweet.services.TwitterStreamProducerService
import play.api.{Application, DefaultApplication}
import play.api.inject._
import play.api.inject.guice.{GuiceApplicationBuilder, GuiceInjectorBuilder}

/**
  * Created by mtomanski on 05.04.16.
  */
object TestHelper {
  val random = new Random()

  def generateAppWithTweets(tweet: Tweet): Application = {
    new GuiceApplicationBuilder().overrides(bind[TwitterStreamProducerService].toInstance(new TwitterStreamProducerServiceMock(tweet))).build()
  }

  def generateTweet(text: String, name: String)= {
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
