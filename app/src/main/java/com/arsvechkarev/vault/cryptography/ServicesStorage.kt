package com.arsvechkarev.vault.cryptography

import com.arsvechkarev.vault.core.model.Service

interface ServicesStorage {
  
  fun getServices(password: String): List<Service>
  
  fun saveService(password: String, service: Service)
  
  fun updateService(password: String, service: Service)
  
  fun deleteService(password: String, service: Service)
}