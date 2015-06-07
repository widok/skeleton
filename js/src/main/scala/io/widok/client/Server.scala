package io.widok.client

import io.widok.common.{Picklers, Protocol}
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

object Server extends autowire.Client[upickle.Js.Value, upickle.Reader, upickle.Writer] {
  def waitingCursor() { dom.window.document.body.style.cursor = "wait" }
  def resetCursor() { dom.window.document.body.style.cursor = "default" }

  override def doCall(req: Request): Future[upickle.Js.Value] = {
    waitingCursor()

    try {
      val future = Ajax.post(
        url = "/api/" + req.path.drop(Protocol.Namespace.length).mkString("/"),
        data = upickle.json.write(upickle.Js.Obj(req.args.toSeq: _*))
      )

      future.onFailure { case error =>
        dom.alert("The server couldn't process the request. Try again later.")
        println(error)
      }

      future.map(x => upickle.json.read(x.responseText))
    } finally {
      resetCursor()
    }
  }

  def read[Result: upickle.Reader](p: upickle.Js.Value) = upickle.readJs[Result](p)
  def write[Result: upickle.Writer](r: Result) = upickle.writeJs(r)
}

object ServerOps {
  import autowire._

  /* IntelliJ erroneously declares this as 'unused import statement'. */
  import upickle._
  import Picklers._

  object Dictionary {
    def timeout(ms: Int) = Server[Protocol].dictionary.timeout(ms).call()

    def lookUp(headword: String) =
      Server[Protocol].dictionary.lookUp(headword).call()
  }
}
