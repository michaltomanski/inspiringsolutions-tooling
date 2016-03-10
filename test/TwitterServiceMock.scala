import akka.stream.scaladsl.Source
import akka.util.ByteString
import services.TwitterStreamService

import scala.concurrent.Future

/**
  * Created by mtomanski on 09.03.16.
  */
class TwitterStreamServiceMock extends TwitterStreamService {
  override def produceStream(trackWord: String): Future[Source[ByteString, Any]] = ???
}
