package domain.interactors

import app.keemobile.kotpass.models.CustomDataValue
import java.time.Instant

internal fun Boolean.toValue(lastModified: Instant? = null): CustomDataValue {
  return CustomDataValue(java.lang.Boolean.toString(this), lastModified)
}

internal fun CustomDataValue.asBoolean(): Boolean {
  return java.lang.Boolean.parseBoolean(value)
}