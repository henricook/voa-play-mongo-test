package repositories

import javax.inject.{ Inject, Singleton }

import models.persisted.UserData
import play.api.libs.json._
import play.modules.reactivemongo.{ ReactiveMongoApi, ReactiveMongoComponents }
import reactivemongo.api.ReadPreference
import reactivemongo.play.json._, collection._

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class UserdataRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit exec: ExecutionContext) extends ReactiveMongoComponents {
  private def userDataCollectionFuture: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("userdata"))

  // Create a Userdata record
  def create(userData: UserData): Future[Unit] = {
    for {
      userDataCollection <- userDataCollectionFuture
      insertResponse <- userDataCollection.insert(userData)
    } yield {
      println("========== Checking!!!")
      if (!insertResponse.ok) {
        throw new Exception("Mongo LastError: %s".format(insertResponse))
      } else {
        ()
      }
    }
  }

  // Retrieve all user data records
  // Note: This method currently exists purely for testing, in a fully spec'ed system it is of course likely
  // that both a write AND a read would be necessary.
  def retrieveAll(): Future[List[UserData]] = {
    for {
      userDataCollection <- userDataCollectionFuture
      find <- userDataCollection.find(Json.obj()).cursor[UserData](ReadPreference.primary).collect[List]()
    } yield find
  }

}
