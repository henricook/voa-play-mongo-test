package repositories

import java.time.LocalDateTime

import models.persisted.UserData
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.{ OneAppPerTest, PlaySpec }
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder

class UserdataRepositorySpec extends PlaySpec with ScalaFutures with OneAppPerTest {
  "Userdata Repository create" should {
    "successfully create a user, and verify that creation" in new TestFixture {
      repo.create(sampleUserData).futureValue

      val result = repo.retrieveAll().futureValue

      result.length mustBe 1
      result.head mustBe sampleUserData
    }
  }

  trait TestFixture {

    val sampleUserData = UserData(
      "John Smith",
      "Male",
      23,
      "AAA",
      LocalDateTime.now
    )

    protected lazy val repo: UserdataRepository = app.injector.instanceOf[UserdataRepository]
  }
}
