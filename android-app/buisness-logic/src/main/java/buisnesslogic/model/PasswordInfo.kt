package buisnesslogic.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a password info that user can have. List of such items is created, encrypted
 * and stored in json format
 *
 * @param id Unique id of the item
 * @param websiteName Name of the website, such as 'Netflix', 'Google', 'Amazon' etc. Cannot be empty
 * @param login Username/email to enter the website. Cannot be empty
 * @param password User's password for the service. Cannot be empty
 * @param notes Additional notes/info for the password. Can be empty
 */
data class PasswordInfo(
  @SerializedName("id")
  val id: String,
  @SerializedName("website")
  val websiteName: String,
  @SerializedName("login")
  val login: String,
  @SerializedName("password")
  val password: String,
  @SerializedName("notes")
  val notes: String,
)
