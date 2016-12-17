package models.persisted

import java.time._

import play.api.libs.json.{ Json, OFormat }
import reactivemongo.bson.{ BSONDateTime, BSONDocument, BSONHandler, BSONReader, BSONWriter, Macros }

case class UserData(name: String, sex: String, age: Int, country: String, createdDate: LocalDateTime)



object UserData {

  implicit object LocalDateTimeFormat extends BSONWriter[LocalDateTime, BSONDateTime] with BSONReader[BSONDateTime, LocalDateTime] {
    def read(bsonDateTime: BSONDateTime): LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(bsonDateTime.value), ZoneId.systemDefault())
    def write(localDateTime: LocalDateTime): BSONDateTime = BSONDateTime(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli)
  }

  implicit val userDataFormat: OFormat[UserData] = Json.format[UserData]
  implicit val userDataBsonFormat: BSONHandler[BSONDocument, UserData] = Macros.handler[UserData]

  def apply(userData: models.UserData): UserData = UserData(
    userData.name,
    userData.sex,
    userData.age,
    userData.country,
    LocalDateTime.now
  )
}