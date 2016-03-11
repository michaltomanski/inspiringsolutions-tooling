import java.io.{ByteArrayOutputStream, ObjectOutputStream}

import akka.stream.scaladsl.Source
import akka.util.ByteString
import models.Tweet
import services.TwitterStreamService

import scala.concurrent.ExecutionContext.Implicits.global


import scala.concurrent.Future

/**
  * Created by mtomanski on 09.03.16.
  */
class TwitterStreamServiceMock extends TwitterStreamService {

  override def produceStream(trackWord: String): Future[Source[ByteString, Any]] = {
    println(" -------------- PRODUCE STREAM STARTET MOCK")
    val tweet = Tweet.emptyTweet
    Future( Source(List(toByteString(tweet))) )
  }

  private def toByteString(obj: Object): ByteString = {
    val ba = new ByteArrayOutputStream()
    val out = new ObjectOutputStream(ba)
    out.writeObject(obj)
    out.close()
    ByteString(ba.toByteArray)
  }

}
