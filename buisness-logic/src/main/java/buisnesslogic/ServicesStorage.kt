package buisnesslogic

import buisnesslogic.model.ServiceEntity

/**
 * Storage that provides basic CRUD operations for [ServiceEntity]
 */
interface ServicesStorage {
  
  /**
   * Returns list of all saved services
   *
   * @param password Master password for encryption/decryption
   */
  fun getServices(password: String): List<ServiceEntity>
  
  /**
   * Saves new service entity
   *
   * @param password Master password for encryption/decryption
   */
  fun saveService(password: String, serviceEntity: ServiceEntity)
  
  /**
   * Updates existing service entity. [serviceEntity] should have the same id as the entity that we
   * want to update
   *
   * @param password Master password for encryption/decryption
   */
  fun updateService(password: String, serviceEntity: ServiceEntity)
  
  /**
   * Deletes existing service entity. [serviceEntity] should have the same id as the entity that we
   * want to delete
   *
   * @param password Master password for encryption/decryption
   */
  fun deleteService(password: String, serviceEntity: ServiceEntity)
}