import Dependencies._

ThisBuild / scalaVersion := "2.13.1"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.payments"
ThisBuild / organizationName := "payments"

lazy val root = (project in file("."))
  .settings(
    name := "fp-payment-app",
    libraryDependencies += scalaTest % Test
  )

val http4sVersion = "0.21.3"

libraryDependencies ++= Seq(
  "org.typelevel"     %% "cats-core"           % "2.1.1",
  "org.typelevel"     %% "cats-effect"         % "2.1.3",
  "org.http4s"        %% "http4s-dsl"          % http4sVersion,
  "org.http4s"        %% "http4s-blaze-server" % http4sVersion,
  "org.http4s"        %% "http4s-circe"        % http4sVersion,
  "org.tpolecat"      %% "doobie-core"         % "0.8.8",
  "io.circe"          %% "circe-core"          % "0.12.3",
  "io.circe"          %% "circe-generic"       % "0.12.3",
  "io.chrisdavenport" %% "log4cats-slf4j"      % "1.1.1",
  "ch.qos.logback"     % "logback-classic"     % "1.1.3"
)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps",
  "-language:higherKinds",
  "-Ywarn-unused"
)
