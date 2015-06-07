package io.widok.client.pages

import org.widok._
import org.widok.bindings.{FontAwesome => fa}
import org.widok.bindings.Bootstrap._
import org.widok.html._

import io.widok.client._
import io.widok.common.Response

import scalajs.concurrent.JSExecutionContext.Implicits.runNow

case class Dictionary() extends CustomPage with DefaultHeader {
  val initial  = Var(true)
  val result   = Var("")
  val headword = Var("")

  headword.filterCycles.filter(_.nonEmpty).attach { hw =>
    ServerOps.Dictionary.lookUp(hw).foreach {
      case Response.Success(r) =>
        result := r.text
      case _ =>
    }

    initial  := false
    headword := ""
  }

  val iptText = Input.Text()

  def body() = Inline(
    Panel(
      Panel.Heading("Dictionary")

    , Panel.Body(
        Alert(b("[Info]"), " Please enter headword!")
          .style(Style.Info)
          .show(initial)

      , result.map(raw)
      )

    , Panel.Footer(
        InputGroup(
          iptText
            .placeholder("Look up word….")
            .size(Size.Small)
            .bindEnter(headword)
            .autofocus(true)

        , span(
            Button(fa.Send(), " Send")
              .style(Style.Primary)
              .size(Size.Small)
              .onClick(_ => iptText.enterValue.produce())
          ).css("input-group-btn")
        )
      )
    ).style(Style.Default)
  )

  def ready(route: InstantiatedRoute) {
    headword := route.args.getOrElse("word", "")

    Application.preload.foreach { response =>
      println(s"Preloaded data: $response")
    }
  }
}
