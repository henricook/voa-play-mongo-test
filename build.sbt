import sbt.Tests.{ Group, SubProcess }

name := "voa-play-mongo-test"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)

scalaVersion := "2.11.8"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.14",
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0" % "it, test",
  "org.mockito" % "mockito-core" % "2.3.6" % "it, test"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

scalacOptions in ThisBuild ++= Seq("-feature", "-language:postfixOps")

sourceDirectory in IntegrationTest := baseDirectory.value / "it"

scalaSource in IntegrationTest := baseDirectory.value / "it"