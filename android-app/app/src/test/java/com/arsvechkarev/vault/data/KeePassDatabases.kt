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
import java.util.UUID

private val PasswordQwetu1233 = domain.Password.create("qwetu1233")

val BasicDatabase = KeePassDatabase.Ver4x.create(
  rootName = CommonConstants.DEFAULT_DATABASE_NAME,
  meta = Meta(),
  credentials = Credentials.from(PasswordQwetu1233)
).run {
  modifyParentGroup {
    val entries = listOf(
      Entry(
        uuid = UUID.fromString("c13dde14-cb66-4f50-95c0-2d636bf05bcd"),
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
      ),
      Entry(
        uuid = UUID.fromString("3d2f85f1-490a-4290-8d66-7de9f9769858"),
        fields = EntryFields.of(
          Title.key to Plain("test"),
          Notes.key to Plain("my text")
        ),
        customData = mapOf(
          CUSTOM_DATA_TYPE_KEY to CustomDataValue(CUSTOM_DATA_PLAIN_TEXT),
          CUSTOM_DATA_FAVORITE_KEY to CustomDataValue("false"),
        )
      )
    )
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

val NonStandardDatabase = KeePassDatabase.Ver4x.create(
  rootName = CommonConstants.DEFAULT_DATABASE_NAME,
  meta = Meta(),
  credentials = Credentials.from(PasswordQwetu1233)
).run {
  modifyParentGroup {
    val entries = listOf(
      Entry(
        uuid = UUID.fromString("c13dde14-cb66-4f50-95c0-2d636bf05bcd"),
        fields = EntryFields.of(
          Title.key to Plain(""),
          Password.key to Encrypted(fromString("qwerty")),
        ),
      ),
      Entry(
        uuid = UUID.fromString("fe674044-8e7b-4ba2-b316-20bf841d3bcd"),
        fields = EntryFields.of(
          Title.key to Plain(""),
          UserName.key to Plain("lalala"),
        ),
      ),
      Entry(
        uuid = UUID.fromString("3d2f85f1-490a-4290-8d66-7de9f9769858"),
        fields = EntryFields.of(
          Title.key to Plain(""),
          Notes.key to Plain("mynotes1"),
        ),
      ),
      Entry(
        uuid = UUID.fromString("86916deb-74b4-4b1c-9f52-517eaad3aad6"),
        fields = EntryFields.of(
          Title.key to Plain(""),
          Notes.key to Plain("mynotes2"),
        ),
      )
    )
    copy(entries = entries)
  }
}
