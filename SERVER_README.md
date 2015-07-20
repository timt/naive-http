naive-http-server
=================
A really simple http server library implemented in scala with no dependencies

Server Usage
------------
Starting a server

    import io.shaka.http.HttpServer
    import io.shaka.http.Response.respond
    import io.shaka.http.Response.seeOther
    val httpServer = HttpServer(request => respond("Hello World!")).start()
    ...
    val httpServer = HttpServer(8080).handler(request => respond("Hello World!")).start()

Handling requests

    import io.shaka.http.RequestMatching._
    httpServer.handler{
        case GET("/hello") => respond("Hello world")
        case GET(echoUrl) => respond(echoUrl)
        case request@POST("/some/restful/thing") => respond(...)
        case req@GET(_) if req.accepts(APPLICATION_JSON) => respond("""{"hello":"world"}""").contentType(APPLICATION_JSON)
        case GET(url"/tickets/$ticketId?messageContains=$messageContains") => respond(s"Ticket $ticketId, messageContains $messageContains").contentType(TEXT_PLAIN)
        case GET("/nothingToSeeHere") => seeOther("http://moveAlong")
        case _ => respond("doh!").status(NOT_FOUND)
    }

Serving static content

    import io.shaka.http.StaticResponse.static
    import io.shaka.http.StaticResponse.classpathDocRoot
    httpServer.handler{
        case GET(path) => static("/home/timt/docRoot", path)
        case GET(path) => static(classpathDocRoot("web"), path)
    }


Stopping the server

    httpServer.stop()

Start an SSL server

    import io.shaka.http.HttpServer
    import io.shaka.http.PathAndPassword
    import io.shaka.http.Response.respond
    val server = HttpServer.https(
        keyStoreConfig = PathAndPassword("src/test/resources/certs/keystore-testing.jks", "password")
    ).handler(_ => respond("Hello world")).start()

Start an SSL with mutual SSL auth

    import io.shaka.http.HttpServer
    import io.shaka.http.PathAndPassword
    import io.shaka.http.Response.respond
    val server = HttpServer.httpsMutualAuth(
        keyStoreConfig = PathAndPassword("src/test/resources/certs/keystore-testing.jks", "password"),
        trustStoreConfig = PathAndPassword("src/test/resources/certs/server-truststore.jks", "password")
    ).handler(_ => respond("Hello world")).start()


For more examples see

* [HttpServerSpec.scala](https://github.com/timt/naive-http/blob/master/src/test/scala/io/shaka/http/HttpServerSpec.scala)
* [ServingStaticResourcesSpec.scala](https://github.com/timt/naive-http/blob/master/src/test/scala/io/shaka/http/ServingStaticResourcesSpec.scala)
* [SslAuthSpec.scala](https://github.com/timt/naive-http/blob/master/src/test/scala/io/shaka/http/SslAuthSpec.scala)
* [MutualSslAuthSpec.scala](https://github.com/timt/naive-http/blob/master/src/test/scala/io/shaka/http/MutualSslAuthSpec.scala)

