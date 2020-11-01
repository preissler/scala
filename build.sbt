name := "scala"

version := "0.1"

scalaVersion := "2.12.12"

resolvers ++= Seq(Resolver.mavenLocal)
lazy val AkkaVersion = "2.6.10"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.4.0",
  "org.typelevel" %% "cats-effect" % "2.1.4",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % AkkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)
