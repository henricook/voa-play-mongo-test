package controllers

import connectors.restcountries.Country
import models.persisted.UserData
import org.mockito.ArgumentCaptor
import org.scalatestplus.play.OneAppPerSuite
import play.api.i18n.MessagesApi
import play.api.mvc.{ Result, Results }
import play.api.test.{ FakeRequest, Helpers }
import repositories.UserdataRepository
import services.CountryService
import testkit.TestBase
import play.api.test.Helpers._
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ApplicationControllerSpec extends TestBase with Results with OneAppPerSuite {
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

      val result: Future[Result] = controller.success.apply(FakeRequest().withFlash("name" -> testName))
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
    "Return a 400 when the form is not present" in new TestFixture {
      val fakeRequest = FakeRequest(Helpers.POST, "/")

      val result: Future[Result] = controller.applicationPost.apply(fakeRequest)
      val bodyText: String = contentAsString(result)

      bodyText must include("This field is required")
    }

    "Return a 400 when the form has errors" in new TestFixture {
      val fakeRequest = FakeRequest(Helpers.POST, "/").withFormUrlEncodedBody(
        "name" -> "Peter Pettigrew",
        "sex" -> "Make believe gender",
        "age" -> "23",
        "country" -> "AAA"
      )

      val result: Future[Result] = controller.applicationPost.apply(fakeRequest)
      val bodyText: String = contentAsString(result)

      bodyText must include("Please select a valid sex")
    }

    "Redirect to success page and save new application when form is correct" in new TestFixture {

      val formContent = Map(
        "name" -> "Minerva McGonagall",
        "sex" -> "Female",
        "age" -> "23",
        "country" -> "AAA"
      )

      when(mockUserdataRepository.create(any[UserData]())).thenReturn(Future.successful(()))

      val fakeRequest = FakeRequest(Helpers.POST, "/").withFormUrlEncodedBody(formContent.toSeq: _*)

      val result: Future[Result] = controller.applicationPost.apply(fakeRequest)

      status(result) mustBe SEE_OTHER

      val userDataCaptor: ArgumentCaptor[UserData] = ArgumentCaptor.forClass(classOf[UserData])

      verify(mockUserdataRepository, times(1)).create(userDataCaptor.capture())

      val capturedUserData = userDataCaptor.getValue

      capturedUserData.name mustBe formContent("name")
      capturedUserData.sex mustBe formContent("sex")
      capturedUserData.age mustBe formContent("age").toInt
      capturedUserData.country mustBe formContent("country")
    }
  }

  trait TestFixture {
    protected val messagesApi: MessagesApi = app.injector.instanceOf[MessagesApi]
    protected val mockCountryService: CountryService = mock[CountryService]
    protected val mockUserdataRepository: UserdataRepository = mock[UserdataRepository]

    when(mockCountryService.getEuropeanCountries).thenReturn(Future.successful(
      List(
        Country("AAA", "Country 1"),
        Country("BBB", "Country 2")
      )
    ))

    protected val controller: ApplicationController = new ApplicationController(
      messagesApi,
      mockCountryService,
      mockUserdataRepository
    )
  }
}
