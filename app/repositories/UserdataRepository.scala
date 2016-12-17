package repositories

import javax.inject.{ Inject, Singleton }

import models.UserData
import play.modules.reactivemongo.{ ReactiveMongoApi, ReactiveMongoComponents }
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class UserdataRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit exec: ExecutionContext) extends ReactiveMongoComponents {
  private def userDataCollectionFuture: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("userdata"))

  def create(userData: UserData): Future[Unit] = {
    for {
      userDataCollection <- userDataCollectionFuture
      lastError <- userDataCollection.insert(userData)
    } yield {
      throw new Exception("Mongo LastError: %s".format(lastError))
    }
  }

}
