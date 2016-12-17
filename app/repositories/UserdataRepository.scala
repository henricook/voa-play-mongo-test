package repositories

import javax.inject.Inject

import models.UserData
import play.modules.reactivemongo.{ ReactiveMongoApi, ReactiveMongoComponents }
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ ExecutionContext, Future }

class UserdataRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit exec: ExecutionContext) extends ReactiveMongoComponents {
  def userDataCollectionFuture: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("userdata"))

  def create(userData: UserData) = {
    for {
      userDataCollection <- userDataCollectionFuture
      lastError <- userDataCollection.insert(userData)
    } yield {
      "Mongo LastError: %s".format(lastError)
    }
  }

}
