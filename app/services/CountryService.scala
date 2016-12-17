package services

import javax.inject.Inject
import javax.inject.Singleton

import com.google.inject.ImplementedBy
import connectors.restcountries.Country

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import language.postfixOps

@Singleton
class CountryService @Inject()(countryClient: connectors.restcountries.Client) {
  lazy val getEuropeanCountries: Future[List[Country]] = countryClient.getEuropeanCountries
}
