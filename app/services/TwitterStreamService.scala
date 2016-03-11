package services

import akka.stream.scaladsl.Source
import akka.util.ByteString

import scala.concurrent.Future

/**
  * Created by mtomanski on 10.03.16.
  */
trait TwitterStreamService {
  def produceStream(trackWord: String): Future[Source[ByteString, Any]]
}

