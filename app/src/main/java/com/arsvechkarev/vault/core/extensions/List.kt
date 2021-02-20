package com.arsvechkarev.vault.core.extensions

import org.json.JSONArray
import org.json.JSONObject

fun <T> JSONArray.transformToList(mapper: (JSONObject) -> T): List<T> {
  val list = ArrayList<T>()
  for (i in 0 until length()) {
    list.add(mapper(getJSONObject(i)))
  }
  return list
}