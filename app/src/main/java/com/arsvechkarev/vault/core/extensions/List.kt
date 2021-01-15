package com.arsvechkarev.vault.core.extensions

import org.json.JSONArray
import org.json.JSONObject

fun JSONArray.forEachObject(block: (JSONObject) -> Unit) {
  for (i in 0 until length()) {
    block(getJSONObject(i))
  }
}

fun <T> JSONArray.transformToList(mapper: (JSONObject) -> T): List<T> {
  val list = ArrayList<T>()
  forEachObject { list.add(mapper(it)) }
  return list
}