import scala.Some
import scala.util.Try
import bintray.Keys._


name := "naive-http"

organization := "io.shaka"

version := Try(sys.env("LIB_VERSION")).getOrElse("1")

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.10.4", "2.11.2")

libraryDependencies ++= Seq(
  "net.databinder" %% "unfiltered-filter" % "0.7.1" % "test",
  "net.databinder" %% "unfiltered-jetty" % "0.7.1" % "test",
  "org.eclipse.jetty.orbit" % "javax.servlet" % "2.5.0.v201103041518" % "test",
  "org.scalatest" %% "scalatest" % "2.2.0" % "test"
)

pgpPassphrase := Some(Try(sys.env("SECRET")).getOrElse("goaway").toCharArray)

pgpSecretRing := file("./publish/sonatype.asc")

bintrayPublishSettings

repository in bintray := "repo"

bintrayOrganization in bintray := None

publishMavenStyle := true

publishArtifact in Test := false

homepage := Some(url("https://github.com/timt/naive-http"))

licenses +=("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

pomExtra :=
    <scm>
      <url>git@github.com:timt/naive-http.git</url>
      <connection>scm:git:git@github.com:timt/naive-http.git</connection>
    </scm>
    <developers>
      <developer>
        <id>timt</id>
      </developer>
    </developers>
