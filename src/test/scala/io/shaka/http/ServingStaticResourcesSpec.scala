package io.shaka.http

import java.io.File
import java.nio.file.Files.readAllBytes
import io.shaka.http.ContentType.{TEXT_CSS, TEXT_CSV, TEXT_HTML}
import io.shaka.http.Http._
import io.shaka.http.HttpServerSpecSupport.withHttpServer
import io.shaka.http.Request.GET
import io.shaka.http.Status.NOT_FOUND
import org.scalatest.FunSuite
import io.shaka.http.StaticResponse.{classpathDocRoot, static}
import scala.io.Source.fromFile

class ServingStaticResourcesSpec extends FunSuite {
  val docRoot = "./src/test/resources/web"

  test("can serve a static file from filesystem") {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(docRoot, path)
      }
      val response = http(GET(s"$rootUrl/test.html"))
      assert(response.entityAsString === fromFile(s"$docRoot/test.html").mkString)
    }
  }

  test("can serve image file from filesystem"){
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(docRoot, path)
      }
      val response = http(GET(s"$rootUrl/clocks.png"))
      assert(response.entity.get.content === readAllBytes(new File(s"$docRoot/clocks.png").toPath))
    }
  }

  test("can serve pdf file from filesystem"){
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(docRoot, path)
      }
      val response = http(GET(s"$rootUrl/specification.pdf"))
      assert(response.entity.get.content === readAllBytes(new File(s"$docRoot/specification.pdf").toPath))
    }
  }

  test("return NOT_FOUND when static resource does not exist") {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(docRoot, path)
      }
      assert(http(GET(s"$rootUrl/test2.html")).status === NOT_FOUND)
    }

  }

  test("correctly set content-type when serving static files") {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(docRoot, path)
      }
      assert(http(GET(s"$rootUrl/test.html")).contentType.get === TEXT_HTML)
      assert(http(GET(s"$rootUrl/test.css")).contentType.get === TEXT_CSS)
      assert(http(GET(s"$rootUrl/testdir/test.csv")).contentType.get === TEXT_CSV)
    }
  }

  test("shows directory listing when serving static resources") {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(docRoot, path)
      }
      val response = http(GET(s"$rootUrl/testdir"))
      assert(response.entityAsString.split("\n").sorted === List("test.csv", "testsubdir", "test.txt").sorted)
    }
  }

  test("can serve a static file from the (file:) classpath") {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(classpathDocRoot("web"), path)
      }
      val response = http(GET(s"$rootUrl/test.html"))
      assert(response.entityAsString === fromFile(s"$docRoot/test.html").mkString)
    }
  }

  test("shows directory listing when serving static resources from (file:) classpath") {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(classpathDocRoot("web"), path)
      }
      val response = http(GET(s"$rootUrl/testdir"))
      assert(response.entityAsString.split("\n").sorted === List("test.csv", "testsubdir", "test.txt").sorted)
    }
  }

  test("can serve a html file from a jar") {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(classpathDocRoot("org/shaka/docroot"), path)
      }
      val response = http(GET(s"$rootUrl/test.html"))
      assert(response.entityAsString === fromFile(s"$docRoot/test.html").mkString)
    }
  }

  test("can serve a image from a jar") {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(classpathDocRoot("org/shaka/docroot"), path)
      }
      val response = http(GET(s"$rootUrl/clocks.png"))
      assert(response.entity.get.content === readAllBytes(new File(s"$docRoot/clocks.png").toPath))
    }
  }

  test("return 404 NOT_FOUND when not static resource not found") {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler{
        case GET(path) => static(classpathDocRoot("org/shaka/docroot"), path)
      }
      val response = http(GET(s"$rootUrl/thisDoesNotExist.html"))
      assert(response.status === NOT_FOUND)
    }
  }

}
