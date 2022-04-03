package com.arsvechkarev.vault.core.model

import android.os.Parcelable
import buisnesslogic.model.ServiceEntity
import com.arsvechkarev.vault.recycler.DifferentiableItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceModel(
    override val id: String,
    val serviceName: String,
    val username: String,
    val email: String,
    val password: String,
) : DifferentiableItem, Parcelable

fun ServiceEntity.toServiceModel(): ServiceModel {
    return ServiceModel(id, serviceName, username, email, password)
}

fun ServiceModel.toServiceEntity(): ServiceEntity {
    return ServiceEntity(id, serviceName, username, email, password)
}

fun List<ServiceEntity>.toServiceModelList(): List<ServiceModel> {
    return map { ServiceModel(it.id, it.serviceName, it.username, it.email, it.password) }
}

fun List<ServiceModel>.toServiceEntityList(): List<ServiceEntity> {
    return map { ServiceEntity(it.id, it.serviceName, it.username, it.email, it.password) }
}