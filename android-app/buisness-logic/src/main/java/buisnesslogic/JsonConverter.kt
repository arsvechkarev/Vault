package buisnesslogic

/**
 * Converter that helps converting list of object to string and vice versa
 */
interface JsonConverter {

    /**
     * Parses [json] to json array the converts that array to list using [mapper]
     *
     * @param mapper Mapper that converts json object represented as map to desired object of type [T]
     */
    fun <T> getFromString(json: String, mapper: (map: Map<String, String>) -> T): List<T>

    /**
     * Converts list of object to json array and then to json string
     *
     * @param converter Converter that converts objects of type [T] to map of values to keys. That map
     * is used to create json objects, collect them into json array and return that array as json string
     */
    fun <T> convertToString(list: List<T>, converter: (element: T) -> Map<String, String>): String
}