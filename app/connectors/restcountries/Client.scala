package connectors.restcountries

import javax.inject.Inject

import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Client @Inject()(ws: WSClient) {

  val baseUrl = "https://restcountries.eu/rest/v1/region"
  val europeanCountriesUrl = s"$baseUrl/Europe"

  def getEuropeanCountries: Future[List[Country]] = {
    ws.url(europeanCountriesUrl).get().map { response =>
      response.json.as[List[Country]]
    }
  }

}
