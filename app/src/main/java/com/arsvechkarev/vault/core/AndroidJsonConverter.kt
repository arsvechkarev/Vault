package com.arsvechkarev.vault.core

import com.arsvechkarev.vault.core.extensions.transformToList
import org.json.JSONArray

object AndroidJsonConverter : JsonConverter {
  
  override fun <T> getFromString(json: String, mapper: (map: Map<String, String>) -> T): List<T> {
    val jsonArray = JSONArray(json)
    return jsonArray.transformToList { jsonObject ->
      val map = HashMap<String, String>()
      jsonObject.keys().forEach { key ->
        map[key] = jsonObject[key].toString()
      }
      return@transformToList mapper(map)
    }
  }
  
  override fun <T> convertToString(list: List<T>, converter: (element: T) -> Map<String, String>): String {
    val jsonArray = JSONArray()
    list.forEach { element ->
      jsonArray.put(converter(element))
    }
    return jsonArray.toString()
  }
}