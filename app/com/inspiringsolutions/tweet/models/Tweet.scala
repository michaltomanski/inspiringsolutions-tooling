package com.inspiringsolutions.tweet.models

import play.api.libs.json.Json

import scala.beans.BeanProperty

case class SimplifiedTweet(
                          id: String,
                          text: String,
                          user: String,
                          userImg: String
                          )

object SimplifiedTweet {
  implicit val jsonFormat = Json.format[SimplifiedTweet]
}

case class Tweet(
                  @BeanProperty contributors: Option[Seq[Contributor]] = None,
                  @BeanProperty coordinates: Option[Coordinates] = None,
                  @BeanProperty created_at: Option[String] = None,
                  @BeanProperty current_user_retweet: Option[Map[String, String]] = None,
                  @BeanProperty entities: Entities,
                  @BeanProperty favorite_count: Option[Int] = None,
                  @BeanProperty favorited: Option[Boolean] = None,
                  @BeanProperty filter_level: Option[String] = None,
                  @BeanProperty id: Long,
                  @BeanProperty id_str: String,
                  @BeanProperty in_reply_to_screen_name: Option[String] = None,
                  @BeanProperty in_reply_to_status_id: Option[Long] = None,
                  @BeanProperty in_reply_to_status_id_str: Option[String] = None,
                  @BeanProperty in_reply_to_user_id: Option[Long] = None,
                  @BeanProperty in_reply_to_user_id_str: Option[String] = None,
                  @BeanProperty lang: Option[String] = None,
                  @BeanProperty place: Option[Place] = None,
                  @BeanProperty possibly_sensitive: Option[Boolean] = None,
                  @BeanProperty quoted_status_id: Option[Long] = None,
                  @BeanProperty quoted_status_id_str: Option[String] = None,
                  @BeanProperty quoted_status: Option[Tweet] = None,
                  @BeanProperty scopes: Option[Map[String, String]] = None,
                  @BeanProperty retweet_count: Int = 0,
                  @BeanProperty retweeted: Option[Boolean] = None,
                  @BeanProperty retweeted_status: Option[Tweet] = None,
                  @BeanProperty source: String,
                  @BeanProperty text: String,
                  @BeanProperty truncated: Boolean,
                  @BeanProperty user: Users,
                  @BeanProperty withheldCopyright: Option[Boolean] = None,
                  @BeanProperty withheldInCountries: Option[Seq[String]] = None,
                  @BeanProperty withheldScope: Option[String] = None
                )

case class Users(
                  @BeanProperty contributors_enabled: Option[Boolean] = None,
                  @BeanProperty created_at: Option[String] = None,
                  @BeanProperty default_profile: Boolean,
                  @BeanProperty default_profile_image: Boolean,
                  @BeanProperty description: Option[String] = None,
                  @BeanProperty entities: Entities,
                  @BeanProperty favourites_count: Int = 0,
                  @BeanProperty follow_request_sent: Option[Boolean] = None,
                  @BeanProperty following: Option[Boolean] = None,
                  @BeanProperty followers_count: Int = 0,
                  @BeanProperty friends_count: Int = 0,
                  @BeanProperty geo_enabled: Boolean = false,
                  @BeanProperty id: Long,
                  @BeanProperty id_str: String,
                  @BeanProperty is_translator: Boolean = false,
                  @BeanProperty lang: String,
                  @BeanProperty listed_count: Int = 0,
                  @BeanProperty location: Option[String] = None,
                  @BeanProperty name: String,
                  @BeanProperty notifications: Option[Boolean] = None,
                  @BeanProperty profile_background_color: String = "",
                  @BeanProperty profile_background_image_url: String = "",
                  @BeanProperty profile_background_image_url_https: String = "",
                  @BeanProperty profile_background_tile: Boolean =false,
                  @BeanProperty profile_banner_url: Option[String] = None,
                  @BeanProperty profile_image_url: String = "",
                  @BeanProperty profile_image_url_https: String = "",
                  @BeanProperty profile_link_color: String = "",
                  @BeanProperty profile_sidebar_border_color: String = "",
                  @BeanProperty profile_sidebar_fill_color: String = "",
                  @BeanProperty profile_text_color: String = "",
                  @BeanProperty profile_use_background_image: Boolean = false,
                  @BeanProperty `protected`: Boolean = false,
                  @BeanProperty screen_name: String,
                  @BeanProperty show_all_inline_media: Option[Boolean] = None,
                  @BeanProperty status: Option[Tweets] = None,
                  @BeanProperty statuses_count: Int = 0,
                  @BeanProperty time_zone: Option[String] = None,
                  @BeanProperty url: Option[String] = None,
                  @BeanProperty utc_offset: Option[Int] = None,
                  @BeanProperty verified: Boolean,
                  @BeanProperty withheld_in_countries: Option[String] = None,
                  @BeanProperty withheld_scope: Option[String] = None
                )

case class Tweets(
                   contributors: Seq[Contributor],
                   coordinates: Coordinates,
                   created_at: String,
                   current_user_retweet: Option[Map[String, String]],
                   entities: Entities,
                   favorite_count: Option[Int],
                   favorited: Option[Boolean],
                   filter_level: String,
                   id: Long,
                   id_str: String,
                   in_reply_to_screen_name: Option[String],
                   in_reply_to_status_id: Option[Long],
                   in_reply_to_status_id_str: Option[String],
                   in_reply_to_user_id: Option[Long],
                   in_reply_to_user_id_str: Option[String],
                   lang: Option[String],
                   place: Option[Place],
                   possibly_sensitive: Option[Boolean],
                   quoted_status_id: Option[Long],
                   quoted_status_id_str: Option[String],
                   quoted_status: Option[Tweet],
                   scopes: Option[Map[String, String]],
                   retweet_count: Int,
                   retweeted: Option[Boolean],
                   retweeted_status: Option[Tweet],
                   source: String,
                   text: String,
                   truncated: Boolean,
                   user: Users,
                   withheld_copyright: Option[Boolean],
                   withheld_in_countries: Option[Seq[String]],
                   withheld_scope: Option[String]
                 )
case class Place(
                  attributes: Map[String, String],
                  bounding_box: BoundingBox,
                  country: String,
                  country_code: String,
                  full_name: String,
                  id: String,
                  name: String,
                  place_type: String,
                  url: String
                )
case class Media(
                  display_url: String,
                  expanded_url: String,
                  id: Long,
                  id_str: String,
                  indices: Seq[Int],
                  media_url: String,
                  media_url_https: String,
                  sizes: Sizes,
                  source_status_id: Option[Long],
                  source_status_id_str: Option[String],
                  `type`: String,
                  url: String
                )
case class BoundingBox(coordinates: Seq[Seq[Seq[Float]]], `type`: String)
case class Contributor(id: Long, id_str: String, screen_name: String)
case class Coordinates(coordinates: Seq[Float], `type`: String)
case class Entities(hashtags: Seq[Hashtag] = Seq(), media: Seq[Media] = Seq(), urls: Seq[Url] = Seq(), user_mentions: Option[Seq[UserMention]] = None)
case class Sizes(thumb: Size, large: Size, medium: Size, small: Size)
case class Size(h: Int, w: Int, resize: String)
case class Hashtag(indices: Seq[Int], text: String)
case class Url(display_url: String, expanded_url: String, indices: Seq[Int], url: String)
case class UserMention(id: Long, id_str: String, indices: Seq[Int], name: String, screen_name: String)

case class LimitNotice(limit: Limit)
case class Limit(track: Long, timestamp_ms: String)