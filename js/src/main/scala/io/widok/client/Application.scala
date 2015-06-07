package io.widok.client

import org.widok._

object Routes {
  val dictionary = Route("/", pages.Dictionary)
  val dictionaryLookUp = Route("/:word", pages.Dictionary)
  val notFound = Route("/404", pages.NotFound)

  val routes = Set(
    dictionary, dictionaryLookUp, notFound
  )
}

object Application extends RoutingApplication(Routes.routes, Routes.notFound) {
  val preload = ServerOps.Dictionary.timeout(5000)
}
