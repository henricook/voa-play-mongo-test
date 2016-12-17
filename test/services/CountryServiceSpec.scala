package services

import connectors.restcountries.{ Client, Country }
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec

import scala.concurrent.Future

class CountryServiceSpec extends PlaySpec with ScalaFutures with MockitoSugar {
  "Get european countries" must {
    "Call the country client to get a list of countries" in new TestFixture {
      val mockCountryList = List(
        Country("AAA", "A Country"),
        Country("AAB", "B Country"),
        Country("CCC", "C Country")
      )

      when(mockCountryClient.getEuropeanCountries).thenReturn(Future.successful(
       mockCountryList
      ))

      val result: List[Country] = countryService.getEuropeanCountries.futureValue

      verify(mockCountryClient, times(1)).getEuropeanCountries

      result mustBe mockCountryList
    }
  }

  trait TestFixture {
    protected val mockCountryClient: Client = mock[connectors.restcountries.Client]

    protected val countryService: CountryService = new CountryService(mockCountryClient)
  }
}
