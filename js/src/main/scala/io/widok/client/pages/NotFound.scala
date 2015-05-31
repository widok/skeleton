package io.widok.client.pages

import org.widok._

import io.widok.client._

case class NotFound() extends CustomPage with DefaultHeader {
  def title() = "Not found"
  def body() = "The page you were looking for could not be found."

  def ready(route: InstantiatedRoute) {

  }
}