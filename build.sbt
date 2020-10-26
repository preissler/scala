name := "scala"

version := "0.1"

scalaVersion := "2.12.12"

resolvers ++= Seq(Resolver.mavenLocal)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.4.0",
  "org.typelevel" %% "cats-effect" % "2.1.4",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)
