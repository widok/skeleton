package io.widok.server

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.ActorSystem

import spray.routing.SimpleRoutingApp

import io.widok.common.{Protocol, Picklers}

class Controllers extends Protocol {
  val dictionary = new controller.Dictionary()
}

object AutowireServer extends autowire.Server[upickle.Js.Value, upickle.Reader, upickle.Writer] {
  def read[Result: upickle.Reader](p: upickle.Js.Value) = upickle.readJs[Result](p)
  def write[Result: upickle.Writer](r: Result) = upickle.writeJs(r)
}

object Server extends SimpleRoutingApp {
  /* IntelliJ erroneously declares this as 'unused import statement'. */
  import Picklers._
  import upickle._

  val impl = new Controllers()
  val router = AutowireServer.route[Protocol](impl)

  def dispatch(path: List[String], args: String): Future[String] = {
    upickle.json.read(args) match {
      case Js.Obj(args @ _*) =>
        val fullPath = Protocol.Namespace ++ path
        router(autowire.Core.Request(fullPath, args.toMap))
          .map(upickle.json.write)
      case _ =>
        Future.failed(new Exception("Arguments need to be a valid JSON object"))
    }
  }

  def main(args: Array[String]): Unit = {
    val port =
      if (args.length >= 1) args(0).toInt
      else 8080

    implicit val system = ActorSystem()

    startServer("0.0.0.0", port = port) {
      get {
        pathSingleSlash {
          getFromFile("web/index.html")
        } ~
        getFromDirectory("web")
      } ~
        post {
          path("api" / Segments) { segments =>
            extract(_.request.entity.asString) { entity =>
              complete {
                dispatch(segments, entity)
              }
            }
          }
        }
    }
  }
}