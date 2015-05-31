package io.widok.common.protocol

import io.widok.common._

trait Dictionary {
  def lookUp(headword: String): Response[model.Dictionary.Result]
}
