package com.arsvechkarev.vault.core

interface JsonConverter {
  
  fun <T> getFromString(json: String, mapper: (map: Map<String, String>) -> T): List<T>
  
  fun <T> convertToString(list: List<T>, converter: (element: T) -> Map<String, String>): String
}