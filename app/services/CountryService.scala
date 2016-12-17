package services

import javax.inject.Inject
import javax.inject.Singleton

import com.google.inject.ImplementedBy
import connectors.restcountries.Country

import scala.concurrent.Await
import scala.concurrent.duration._
import language.postfixOps

@Singleton
class CountryService @Inject()(countryClient: connectors.restcountries.Client) {
  // TODO: Await is normally a bad pattern, can this be done differently?
  val getEuropeanCountries: List[Country] = Await.result(countryClient.getEuropeanCountries, 5 seconds)
}
