package services

import javax.inject.Inject
import javax.inject.Singleton

import connectors.restcountries.Country

import scala.concurrent.Future

@Singleton
class CountryService @Inject()(countryClient: connectors.restcountries.Client) {
  val getEuropeanCountries: Future[List[Country]] = countryClient.getEuropeanCountries
}
