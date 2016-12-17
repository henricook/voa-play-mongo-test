package testkit

import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{ Millis, Seconds, Span }
import org.scalatestplus.play.{ OneAppPerSuite, PlaySpec }
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection
import scala.concurrent.ExecutionContext.Implicits.global

trait ITTestBase extends PlaySpec with ScalaFutures with OneAppPerSuite with BeforeAndAfterEach {
  // Default ScalaFutures patience settings
  implicit val defaultPatience =
    PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  implicit lazy override val app: Application = new GuiceApplicationBuilder().configure(Map(
    "mongodb.db" -> "test-nodetest",
    "mongodb.uri" -> "mongodb://localhost:27017/test-nodetest"
  )).build

  val reactiveMongoApi: ReactiveMongoApi = app.injector.instanceOf[ReactiveMongoApi]

  // Wipe the database before each test
  override def beforeEach(): Unit = {
    reactiveMongoApi.database.map(_.collection[JSONCollection]("userdata")).map { collection =>
      collection.drop(failIfNotFound = false)
    }
  }
}
