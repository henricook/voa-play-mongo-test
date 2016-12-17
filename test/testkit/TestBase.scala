package testkit

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec

trait TestBase extends PlaySpec with ScalaFutures with MockitoSugar
