package io.widok.server.dictionary

import net.sf.extjwnl.dictionary.Dictionary

import scala.collection.JavaConversions._

object WordNet {
  val dictionary = Dictionary.getDefaultResourceInstance

  // TODO Encode HTML
  def lookUp(lemma: String): String = {
    val words = dictionary.lookupAllIndexWords(lemma).getIndexWordArray
    words.map { word =>
      val senses = word.getSenses.map { sense =>
        s"<li>${sense.getGloss}</li>"
      }.mkString

      s"<b>${word.getLemma}</b> (${word.getPOS.getLabel})<ul>$senses</ul>"
    }.mkString
  }
}
