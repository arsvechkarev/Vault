package com.arsvechkarev.vault.core.model

import android.os.Parcelable
import buisnesslogic.model.PasswordInfo
import com.arsvechkarev.vault.recycler.DifferentiableItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class PasswordInfoItem(
  override val id: String,
  val websiteName: String,
  val login: String,
  val password: String,
  val notes: String,
) : DifferentiableItem, Parcelable

fun PasswordInfo.toPasswordInfoItem(): PasswordInfoItem {
  return PasswordInfoItem(id, websiteName, login, notes, password)
}

fun PasswordInfoItem.toPasswordInfo(): PasswordInfo {
  return PasswordInfo(id, websiteName, login, password, notes)
}

fun List<PasswordInfo>.toInfoItemsList(): List<PasswordInfoItem> {
  return map {
    PasswordInfoItem(
      id = it.id,
      websiteName = it.websiteName,
      login = it.login,
      password = it.password,
      notes = it.notes
    )
  }
}

fun List<PasswordInfoItem>.toPasswordInfoList(): List<PasswordInfo> {
  return map {
    PasswordInfo(
      id = it.id,
      websiteName = it.websiteName,
      login = it.login,
      password = it.password,
      notes = it.notes
    )
  }
}
