package buisnesslogic.model

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
  val id: String,
  val websiteName: String,
  val login: String,
  val password: String,
  val notes: String,
)
