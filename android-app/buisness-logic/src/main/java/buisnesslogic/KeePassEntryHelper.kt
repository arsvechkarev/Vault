package buisnesslogic

import app.keemobile.kotpass.models.Entry

val Entry.isDefinitePassword
  get() = customData
      .any { it.key == CUSTOM_DATA_TYPE_KEY && it.value.value == CUSTOM_DATA_PASSWORD }

val Entry.isDefinitePlainText
  get() = customData
      .any { it.key == CUSTOM_DATA_TYPE_KEY && it.value.value == CUSTOM_DATA_PLAIN_TEXT }

val Entry.isProbablePlainText
  get() = fields.notes?.content?.isNotEmpty() == true
      && fields.userName?.content?.isEmpty() == true
      && fields.password?.content?.isEmpty() == true
      && fields.url?.content?.isEmpty() == true