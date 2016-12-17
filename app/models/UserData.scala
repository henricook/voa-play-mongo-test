package models

import java.time.ZonedDateTime

import play.api.libs.json.{ Json, OFormat }

case class UserData(name: String, sex: String, age: Int, country: String)

object UserData {
  implicit val userDataFormat: OFormat[UserData] = Json.format[UserData]
}
