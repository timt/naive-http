naive-http  [![Build Status](https://travis-ci.org/timt/naive-http.png?branch=master)](https://travis-ci.org/timt/naive-http)
==========
A really simple http client library implemented in scala with no dependencies


Requirements
------------

* [scala](http://www.scala-lang.org) 2.10.4

Usage
-----
Add the following lines to your build.sbt

    resolvers += "Tim Tennant's repo" at "http://dl.bintray.com/timt/repo/"

    libraryDependencies += "io.shaka" %% "naive-http" % "18"

    import io.shaka.http.Http.http
    import io.shaka.http.Request.{GET, POST}
    ...
    val response = http(GET("http://www.google.com"))
    ...
    //Add header
    import io.shaka.http.HttpHeader.USER_AGENT
    val response = http(GET("http://www.google.com").header(USER_AGENT,"my agent"))
    ...
    //Post JSON
    import io.shaka.http.ContentType.APPLICATION_JSON
    val response = http(POST("http://some/json/server").contentType(APPLICATION_JSON).entity("""{"foo":"bar"}"""))
    ...
    //Post form parameters
    val response = http(POST("http://some/json/server").entity("""{"foo":"bar"}""").formParameters(FormParameter("name","value")))
    ...
    //Trust all SSL certificates
    import io.shaka.http.TrustAllSslCertificates
    TrustAllSslCertificates

For more examples see [HttpSpec.scala](https://github.com/timt/http/blob/master/src/test/scala/io/shaka/http/HttpSpec.scala)

See [timt/repo](http://dl.bintray.com/timt/repo/io/shaka/naive-html_2.10) for latest released version number


Code license
------------
Apache License 2.0