package io.widok.client

import org.widok._
import org.widok.bindings.Bootstrap._
import pl.metastack.metarx.Var

trait DefaultHeader {
  val stateClosed = Var(true)

  def header() =
    NavigationBar(
      Container(
        NavigationBar.Header(
          NavigationBar.Toggle(stateClosed),
          NavigationBar.Collapse(stateClosed)(
            NavigationBar.Brand(Globals.brandName)
          )
        )
      )
    )
}
