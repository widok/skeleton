package io.widok.server.controller

import io.widok.common.model
import io.widok.common.protocol
import io.widok.common.Response
import io.widok.server.dictionary.WordNet

class Dictionary extends protocol.Dictionary {
  def timeout(ms: Int): Response[String] = {
    Thread.sleep(ms)
    Response.Success(s"Waited $ms ms")
  }

  def lookUp(headword: String): Response[model.Dictionary.Result] =
    Response.Success(
      model.Dictionary.Result(
        WordNet.lookUp(headword)
      )
    )
}
