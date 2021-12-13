package buisnesslogic.model

/**
 * Service entity that represents a service that user can have. List of services is created, encrypted
 * and stored in json format
 *
 * @param id Unique id of the service
 * @param serviceName Name of the service, such as 'Netflix', 'Google', 'Amazon' etc. Cannot be empty
 * @param username Name of the user in the service. Can be empty
 * @param email Email of the user in the service. Can be empty
 * @param password Password of the user in the service. Cannot be empty
 */
data class ServiceEntity(
  val id: String,
  val serviceName: String,
  val username: String,
  val email: String,
  val password: String,
)