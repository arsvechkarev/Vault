package buisnesslogic

import buisnesslogic.model.ServiceEntity

interface ServicesStorage {
  
  fun getServices(password: String): List<ServiceEntity>
  
  fun saveService(password: String, serviceEntity: ServiceEntity)
  
  fun updateService(password: String, serviceEntity: ServiceEntity)
  
  fun deleteService(password: String, serviceEntity: ServiceEntity)
}