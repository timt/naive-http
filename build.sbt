name := "http"

organization := "io.shaka"

version := "5"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
    "net.databinder" %% "unfiltered-filter" % "0.7.1" % "test",
    "net.databinder" %% "unfiltered-jetty" % "0.7.1" % "test",
    "org.eclipse.jetty.orbit" % "javax.servlet" % "2.5.0.v201103041518" % "test",
    "org.scalatest" % "scalatest_2.10" % "2.1.4" % "test"
)

publishTo <<= (version) { version: String =>
  val github = "/Users/timt/Projects/timt.github.com/repo/"
  if (version.trim.endsWith("SNAPSHOT")) Some(Resolver.file("file",  new File( github + "snapshots/")))
  else                                   Some(Resolver.file("file",  new File( github + "releases/")))
}