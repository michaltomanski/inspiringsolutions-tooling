import actors.TwitterService
import com.google.inject.{AbstractModule, Scope}
import controllers.HomeController

/**
  * Created by mtomanski on 09.03.16.
  */
class Module extends AbstractModule  {

  override protected def configure(): Unit = {
    bind(classOf[TwitterService]).to(classOf[TwitterServiceMock])
  }
}
