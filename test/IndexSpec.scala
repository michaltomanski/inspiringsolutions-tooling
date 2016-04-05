//import org.scalatestplus.play._
//import play.api.test.Port
//
//
///**
//  * Created by mtomanski on 05.04.16.
//  */
//class IndexSpec extends PlaySpec with OneServerPerSuite with OneBrowserPerSuite with FirefoxFactory {
//
//  override lazy val port: Port = 9000
//
//  "Testing" must {
//    "test" in {
//      go to s"http://localhost:$port"
//      pageTitle mustBe "Tweet Fetcher (KarieraIt)"
//      eventually { pageTitle mustBe "Tweet Fetcher (KarieraIt)" }
//      assert(1 == 1)
//    }
//  }
//}
