package buisnesslogic

import app.keemobile.kotpass.models.Entry

val Entry.isDefinitePassword
  get() = customData[CUSTOM_DATA_TYPE_KEY]?.value == CUSTOM_DATA_PASSWORD

val Entry.isDefinitePlainText
  get() = customData[CUSTOM_DATA_TYPE_KEY]?.value == CUSTOM_DATA_PLAIN_TEXT

val Entry.isProbablePlainText
  get() = fields.notes?.content?.isNotEmpty() == true
      && fields.userName?.content?.isEmpty() == true
      && fields.password?.content?.isEmpty() == true
      && fields.url?.content?.isEmpty() == true