package com.inspiringsolutions.tweet.services

import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.google.inject.ImplementedBy

import scala.concurrent.Future

/**
  * Created by mtomanski on 10.03.16.
  */
@ImplementedBy(classOf[TwitterStreamProducerServiceImpl])
trait TwitterStreamProducerService {
  def produceStream(trackWord: String): Future[Source[ByteString, Any]]
}

