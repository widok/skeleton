package io.widok.client

import org.widok._
import org.widok.bindings.Bootstrap._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait CustomPage extends Page {
  def header(): Widget[_]
  def body(route: InstantiatedRoute): View
  def render(route: InstantiatedRoute) = Future[View](
    Inline(
      header()
      , Container(body(route))
    )
  )
}