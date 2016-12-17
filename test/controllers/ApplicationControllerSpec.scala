package controllers

import connectors.restcountries.Country
import org.scalatestplus.play.OneAppPerSuite
import play.api.i18n.MessagesApi
import play.api.mvc.{ Result, Results }
import play.api.test.FakeRequest
import repositories.UserdataRepository
import services.CountryService
import testkit.TestBase
import play.api.test.Helpers._
import org.mockito.Mockito._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ApplicationControllerSpec extends TestBase with Results {
  "Application Controller present" must {
    "Display a valid page" in new TestFixture {
      val result: Future[Result] = controller.present.apply(FakeRequest())
      val bodyText: String = contentAsString(result)

      status(result) mustBe OK
      bodyText must include("Some Useful Government Service")
    }
  }

  "Application Controller success" must {
    "Display a success page when a firstname is flashed" in new TestFixture {
      val testName = "Albus Dumbledore"

      val result: Future[Result] = controller.success.apply(FakeRequest().withFlash("name" -> "Albus Dumbledore"))
      val bodyText: String = contentAsString(result)

      bodyText must include(testName)
      bodyText must include("Thankyou for applying to this useful government service")
    }

    "Display an error when accessing without flash data" in new TestFixture {
      intercept[Exception] {
        controller.success.apply(FakeRequest()).futureValue
      }
    }
  }

  "Application Controller application post" must {

  }

  trait TestFixture {
    protected val mockMessagesApi: MessagesApi = mock[MessagesApi]
    protected val mockCountryService: CountryService = mock[CountryService]
    protected val mockUserdataRepository: UserdataRepository = mock[UserdataRepository]

    when(mockCountryService.getEuropeanCountries).thenReturn(Future.successful(
      List(
        Country("AAA", "Country 1"),
        Country("BBB", "Country 2")
      )
    ))

    protected val controller: ApplicationController = new ApplicationController(
      mockMessagesApi,
      mockCountryService,
      mockUserdataRepository
    )
  }
}
