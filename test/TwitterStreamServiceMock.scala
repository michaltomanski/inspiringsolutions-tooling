import akka.stream.scaladsl.Source
import akka.util.ByteString
import services.TwitterStreamService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Created by mtomanski on 09.03.16.
  */
class TwitterStreamServiceMock extends TwitterStreamService {

  override def produceStream(trackWord: String): Future[Source[ByteString, Any]] = {
    Future( Source.tick(0.seconds, 200.millisecond, toByteString(sampleTweetString)) )
  }

  private def toByteString(obj: String): ByteString = {
    ByteString(obj)
  }

  private val sampleTweetString = "{\"created_at\":\"Fri Mar 11 09:58:53 +0000 2016\",\"id\":708230708259639296,\"id_str\":\"708230708259639296\",\"text\":\"Why use java, when you have scala?\",\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\/download\\/iphone\\\" rel=\\\"nofollow\\\"\\u003eTwitter for iPhone\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":708230347415277568,\"in_reply_to_status_id_str\":\"708230347415277568\",\"in_reply_to_user_id\":18077224,\"in_reply_to_user_id_str\":\"18077224\",\"in_reply_to_screen_name\":\"vicesam\",\"user\":{\"id\":361122700,\"id_str\":\"361122700\",\"name\":\"Peer Rails\",\"screen_name\":\"omckws\",\"location\":\"Moscow\",\"url\":\"http:\\/\\/omck.tv\",\"description\":\"I am (NOT) a Ruby Developer. PSN\\/WiiU: railsev. Github PeerRails. \\u043d\\u0435 \\u0431\\u043b\\u0443\\u0434\\u0438\\u043b\",\"protected\":false,\"verified\":false,\"followers_count\":616,\"friends_count\":295,\"listed_count\":31,\"favourites_count\":825,\"statuses_count\":88908,\"created_at\":\"Wed Aug 24 08:44:17 +0000 2011\",\"utc_offset\":-18000,\"time_zone\":\"Quito\",\"geo_enabled\":false,\"lang\":\"en-gb\",\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"1A1B1F\",\"profile_background_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_background_images\\/455070797988503552\\/4Mma69ds.jpeg\",\"profile_background_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_background_images\\/455070797988503552\\/4Mma69ds.jpeg\",\"profile_background_tile\":false,\"profile_link_color\":\"B0820E\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_sidebar_fill_color\":\"252429\",\"profile_text_color\":\"666666\",\"profile_use_background_image\":true,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/706262843214135296\\/OvW4sF5b_normal.jpg\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/706262843214135296\\/OvW4sF5b_normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/361122700\\/1401741355\",\"default_profile\":false,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":0,\"favorite_count\":0,\"entities\":{\"hashtags\":[],\"urls\":[],\"user_mentions\":[{\"screen_name\":\"vicesam\",\"name\":\"sam\",\"id\":18077224,\"id_str\":\"18077224\",\"indices\":[0,8]}],\"symbols\":[]},\"favorited\":false,\"retweeted\":false,\"filter_level\":\"low\",\"lang\":\"ru\",\"timestamp_ms\":\"1457690333796\"}"

}
