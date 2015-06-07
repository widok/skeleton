package io.widok.common.protocol

import io.widok.common._

trait Dictionary {
  def timeout(ms: Int): Response[String]
  def lookUp(headword: String): Response[model.Dictionary.Result]
}
