naive-http 
==========

[![Build Status][badge-build]][link-build]
[![Release Artifacts][badge-release]][link-release]

[comment]: <> ([![Maven Central][badge-maven]][link-maven])

A really simple http library implemented in scala with no dependencies

Requirements
------------

* [scala](http://www.scala-lang.org) 2.13.3
* [scala](http://www.scala-lang.org) 2.12.1

Client Usage
------------
Add the following lines to your build.sbt

[comment]: <> (    resolvers += "Tim Tennant's repo" at "http://dl.bintray.com/timt/repo/")

    libraryDependencies += "io.shaka" %% "naive-http" % "119"

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
    import io.shaka.http.Https.HttpsConfig
    implicit val https: Option[HttpsConfig] = Some(HttpsConfig(TrustAnyServer))
    val response = http(GET("https://someurl"))
    ...
    //Use a trust store containing all the certificates that the client trusts
    import io.shaka.http.Https.TrustServersByTrustStore
    implicit val https = HttpsConfig(TrustServersByTrustStore("src/test/resources/certs/server-truststore.jks", "password"))
    val response = http(GET("https://someurl"))
    ...
    //Use a key store containing the client certificate for connecting to http servers using SSL mutual-auth
    import io.shaka.http.Https.UseKeyStore
    implicit val mutualSslAuth: Option[HttpsConfig] = Some(HttpsConfig(
      TrustServersByTrustStore("src/test/resources/certs/server-truststore.jks", "password"),
      UseKeyStore("src/test/resources/certs/keystore-testing-client.jks", "password")
    ))
    val response = http(GET("https://someurl"))
    ...
    //Basic auth
    val response = http(GET("https://someurl").basicAuth("me","myPassword"))


For more examples see 

* [HttpSpec.scala](https://github.com/timt/naive-http/blob/master/src/test/scala/io/shaka/http/HttpSpec.scala)
* [HttpsSpec.scala](https://github.com/timt/naive-http/blob/master/src/test/scala/io/shaka/http/HttpsSpec.scala)
* [SslAuthSpec.scala](https://github.com/timt/naive-http/blob/master/src/test/scala/io/shaka/http/SslAuthSpec.scala)
* [MutualSslAuthSpec.scala](https://github.com/timt/naive-http/blob/master/src/test/scala/io/shaka/http/MutualSslAuthSpec.scala)

Server Usage
------------
see [SERVER_README.md](SERVER_README.md)


Code license
------------
Apache License 2.0

[badge-build]: https://github.com/timt/naive-http/actions/workflows/build.yml/badge.svg
[link-build]: https://github.com/timt/naive-http/actions/

[badge-release]: https://img.shields.io/nexus/r/https/oss.sonatype.org/io.shaka/naive-http_2.12.svg "Sonatype Releases"
[link-release]: https://oss.sonatype.org/content/repositories/releases/io/shaka/naive-http_2.12/ "Sonatype Releases"

[comment]: <> ([badge-maven]: https://maven-badges.herokuapp.com/maven-central/io.shaka/naive-http_2.12/badge.svg)

[comment]: <> ([link-maven]: https://maven-badges.herokuapp.com/maven-central/io.shaka/naive-http_2.12)
