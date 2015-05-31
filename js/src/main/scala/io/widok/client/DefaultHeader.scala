package io.widok.client

import org.widok._
import org.widok.bindings.Bootstrap._

trait DefaultHeader {
  def header() =
    NavigationBar(
      Container(
        NavigationBar.Header(
          NavigationBar.Toggle(),
          NavigationBar.Brand(Globals.brandName)
        )
      )
    )
}
