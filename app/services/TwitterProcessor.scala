package services

import models.Tweet

/**
  * Created by mtomanski on 10.03.16.
  */
class TwitterProcessor {

  def processTweet(tweet: Tweet): String = {
    s"""----------------
       |Length: ${tweet.text.length} User: ${tweet.user.name} HashTags: ${tweet.entities.hashtags.map(_.text).mkString(", ")}
       |${tweet.text}
       |${tweet.text.reverse}
     """.stripMargin
  }

}
