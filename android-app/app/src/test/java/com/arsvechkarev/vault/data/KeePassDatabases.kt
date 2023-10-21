package com.arsvechkarev.vault.data

import app.keemobile.kotpass.constants.BasicField.Notes
import app.keemobile.kotpass.constants.BasicField.Password
import app.keemobile.kotpass.constants.BasicField.Title
import app.keemobile.kotpass.constants.BasicField.Url
import app.keemobile.kotpass.constants.BasicField.UserName
import app.keemobile.kotpass.cryptography.EncryptedValue.Companion.fromString
import app.keemobile.kotpass.database.Credentials
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.modifiers.modifyEntries
import app.keemobile.kotpass.database.modifiers.modifyParentGroup
import app.keemobile.kotpass.models.CustomDataValue
import app.keemobile.kotpass.models.Entry
import app.keemobile.kotpass.models.EntryFields
import app.keemobile.kotpass.models.EntryValue.Encrypted
import app.keemobile.kotpass.models.EntryValue.Plain
import app.keemobile.kotpass.models.Meta
import domain.CUSTOM_DATA_FAVORITE_KEY
import domain.CUSTOM_DATA_PASSWORD
import domain.CUSTOM_DATA_PLAIN_TEXT
import domain.CUSTOM_DATA_TYPE_KEY
import domain.CommonConstants
import domain.from

private val PasswordQwetu1233 = domain.Password.create("qwetu1233")

val BasicDatabase = KeePassDatabase.Ver4x.create(
  rootName = CommonConstants.DEFAULT_DATABASE_NAME,
  meta = Meta(),
  credentials = Credentials.from(PasswordQwetu1233)
).run {
  modifyParentGroup {
    val entries = buildList {
      add(Entry(
        uuid = uuid,
        fields = EntryFields.of(
          Title.key to Plain("google"),
          UserName.key to Plain("me@gmail.com"),
          Password.key to Encrypted(fromString("qwerty")),
          Url.key to Plain("google.com"),
          Notes.key to Plain("")
        ),
        customData = mapOf(
          CUSTOM_DATA_TYPE_KEY to CustomDataValue(CUSTOM_DATA_PASSWORD),
          CUSTOM_DATA_FAVORITE_KEY to CustomDataValue("false"),
        )
      ))
      add(Entry(
        uuid = uuid,
        fields = EntryFields.of(
          Title.key to Plain("test"),
          Notes.key to Plain("my text")
        ),
        customData = mapOf(
          CUSTOM_DATA_TYPE_KEY to CustomDataValue(CUSTOM_DATA_PLAIN_TEXT),
          CUSTOM_DATA_FAVORITE_KEY to CustomDataValue("false"),
        )
      ))
    }
    copy(entries = entries)
  }
}

val BasicDatabase2 = BasicDatabase.modifyEntries {
  if (this.fields.title!!.content == "google") {
    copy(fields = fields.mapValues {
      if (it.key == "Title") {
        Plain("test")
      } else {
        it.value
      }
    })
  } else {
    this
  }
}
