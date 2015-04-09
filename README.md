naive-http  [![Build Status](https://travis-ci.org/timt/naive-http.png?branch=master)](https://travis-ci.org/timt/naive-http) [ ![Download](https://api.bintray.com/packages/timt/repo/naive-http/images/download.png) ](https://bintray.com/timt/repo/naive-http/_latestVersion)
==========
A really simple http client library implemented in scala with no dependencies


Requirements
------------

* [scala](http://www.scala-lang.org) 2.10.4
* [scala](http://www.scala-lang.org) 2.11.2

Usage
-----
Add the following lines to your build.sbt

    resolvers += "Tim Tennant's repo" at "http://dl.bintray.com/timt/repo/"

    libraryDependencies += "io.shaka" %% "naive-http" % "70"

Start hacking

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
    //Specify a timeout in milli seconds (same value used for both connect and read)
    implicit val timeout = Timeout(1000)
    val response = http(GET("http://www.google.com"))
    ...
    //Specify a proxy
    implicit val proxy = io.shaka.http.proxy("my.proxy.server", 8080)
    val response = http(GET("http://www.google.com"))
    implicit val proxy = io.shaka.http.proxy("my.proxy.server", 8080, "proxyUser", "proxyPassword")
    val response = http(GET("http://www.google.com"))
    ...
    //Trust all SSL certificates (globally)
    import io.shaka.http.Https.TrustAllSslCertificates
    TrustAllSslCertificates
    ...
    //Trust all SSL certificates (non-globally)
    import io.shaka.http.Https.TrustingKeyStore
    implicit val https = Some(TrustingKeyStore("TLS"))
    val response = http(GET("https://someurl"))
    ...
    //Use a key store
    import io.shaka.http.Https.HttpsKeyStore
    implicit val https = Some(HttpsKeyStore("src/test/resources/certs/keystore-testing.jks", "password"))
    val response = http(GET("https://someurl"))
    ...
    //Basic auth
    val response = http(GET("https://someurl").basicAuth("me","myPassword"))


For more examples see 

* [HttpSpec.scala](https://github.com/timt/naive-http/blob/master/src/test/scala/io/shaka/http/HttpSpec.scala)
* [HttpsSpec.scala](https://github.com/timt/naive-http/blob/master/src/test/scala/io/shaka/http/HttpsSpec.scala)


Code license
------------
Apache License 2.0