package buisnesslogic

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser

object GsonJsonConverter : JsonConverter {

    override fun <T> getFromString(json: String, mapper: (map: Map<String, String>) -> T): List<T> {
        val jsonArray = JsonParser.parseString(json) as JsonArray
        val list = ArrayList<T>()
        jsonArray.forEach { jsonElement ->
            val map = HashMap<String, String>()
            (jsonElement as JsonObject).keySet().forEach { key ->
                map[key] = jsonElement[key].asString
            }
            list.add(mapper(map))
        }
        return list
    }

    override fun <T> convertToString(
        list: List<T>,
        converter: (element: T) -> Map<String, String>
    ): String {
        val jsonArray = JsonArray()
        list.forEach { element ->
            val jsonObject = JsonObject()
            val map = converter(element)
            for ((key, value) in map) {
                jsonObject.addProperty(key, value)
            }
            jsonArray.add(jsonObject)
        }
        return jsonArray.toString()
    }
}