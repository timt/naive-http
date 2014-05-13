http
----
A really simple http client library implemented in scala with no dependencies


Requirements
------------

* [scala](http://www.scala-lang.org) 2.10.4

Usage
-----
Add the following lines to your build.sbt

    resolvers += "Tim Tennant's repo" at "http://timt.github.com/repo/releases/"

    libraryDependencies += "io.shaka" %% "http" % "1"

    import io.shaka.http.Http.http
    import io.shaka.http.Request.{GET, POST}
    ...
    val response = http(GET("http://www.google.com"))
    ...
    val postResponse = http(POST("http://some/json/server").entity("""{"foo":"bar"}"""))

For more examples see [HttpSpec.scala](https://github.com/timt/http/blob/master/src/test/scala/io/shaka/http/HttpSpec.scala)

Code license
------------
Apache License 2.0