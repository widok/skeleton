package io.widok.client

import org.widok._
import org.widok.bindings.Bootstrap._

trait CustomPage extends Page {
  def header(): Widget[_]
  def body(): View
  def view() = Inline(
    header()
  , Container(body())
  )
}