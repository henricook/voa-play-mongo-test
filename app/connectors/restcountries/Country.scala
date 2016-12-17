package connectors.restcountries

import play.api.libs.json.{ Json, OFormat }

case class Country(alpha3Code: String, name: String)

object Country {
  implicit val countryFormat: OFormat[Country] = Json.format[Country]
}
