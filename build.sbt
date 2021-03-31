import scala.util.Try

name := "naive-http"

organization := "io.shaka"

version := (Try(sys.env("GITHUB_RUN_NUMBER")).getOrElse("1").toInt + 114).toString

scalaVersion := "2.13.3"

crossScalaVersions := Seq("2.12.1", "2.13.3")

homepage := Some(url("https://github.com/timt/naive-http"))

licenses +=("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

libraryDependencies ++= Seq(
  "org.eclipse.jetty.orbit" % "javax.servlet" % "2.5.0.v201103041518" % "test",
  "org.scalatest" %% "scalatest" % "3.1.2" % "test"
)

developers := List(
  Developer("timt", "Tim Tennant", "ttennant@gmail.com", url("https://github.com/timt"))
)

usePgpKeyHex("timt-ci bot")

publishArtifact in Test := false
