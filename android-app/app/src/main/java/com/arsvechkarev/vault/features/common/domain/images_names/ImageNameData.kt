package com.arsvechkarev.vault.features.common.domain.images_names

sealed class ImageNameData(val imageName: String) {
  
  class Basic(imageName: String) : ImageNameData(imageName)
  
  class Compound(imageName: String, val possibleNames: List<String>) : ImageNameData(imageName)
  
  companion object {
    
    fun parse(string: String): List<ImageNameData> {
      val lines = string.split("\n")
      val list = ArrayList<ImageNameData>()
      for (line in lines) {
        if (line.contains("|")) {
          val possibleNamesString = line.substringBefore("|")
          val possibleNames = possibleNamesString.split(",")
          val imageName = line.substringAfter("|")
          list.add(Compound(imageName, possibleNames))
        } else {
          list.add(Basic(line))
        }
      }
      return list
    }
    
    fun toString(list: List<ImageNameData>): String {
      return buildString {
        for (item in list) {
          when (item) {
            is Basic -> append(item.imageName)
            is Compound -> {
              val possibleNames = item.possibleNames.joinToString(",")
              append(possibleNames)
              append("|")
              append(item.imageName)
            }
          }
          append("\n")
        }
      }.trim('\n')
    }
  }
}
