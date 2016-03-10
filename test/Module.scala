import com.google.inject.AbstractModule
import services.{TwitterService, TwitterStreamService}

/**
  * Created by mtomanski on 09.03.16.
  */
class Module extends AbstractModule  {
  override protected def configure(): Unit = {
    bind(classOf[TwitterStreamService]).to(classOf[TwitterStreamServiceMock])
  }
}
