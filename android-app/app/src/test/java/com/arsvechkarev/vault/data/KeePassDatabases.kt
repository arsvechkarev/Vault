package com.arsvechkarev.vault.data

import app.keemobile.kotpass.constants.BasicField.Notes
import app.keemobile.kotpass.constants.BasicField.Password
import app.keemobile.kotpass.constants.BasicField.Title
import app.keemobile.kotpass.constants.BasicField.Url
import app.keemobile.kotpass.constants.BasicField.UserName
import app.keemobile.kotpass.cryptography.EncryptedValue.Companion.fromString
import app.keemobile.kotpass.database.Credentials
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.decode
import app.keemobile.kotpass.database.modifiers.modifyEntries
import app.keemobile.kotpass.database.modifiers.modifyParentGroup
import app.keemobile.kotpass.models.CustomDataValue
import app.keemobile.kotpass.models.Entry
import app.keemobile.kotpass.models.EntryFields
import app.keemobile.kotpass.models.EntryValue.Encrypted
import app.keemobile.kotpass.models.EntryValue.Plain
import app.keemobile.kotpass.models.Meta
import domain.CUSTOM_DATA_FAVORITE_KEY
import domain.CUSTOM_DATA_NOTE
import domain.CUSTOM_DATA_PASSWORD
import domain.CUSTOM_DATA_TYPE_KEY
import domain.CommonConstants
import domain.from
import java.io.ByteArrayInputStream
import java.util.Base64
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
          CUSTOM_DATA_TYPE_KEY to CustomDataValue(CUSTOM_DATA_NOTE),
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

val DatabaseWithGroups = KeePassDatabase.decode(
  ByteArrayInputStream(
    Base64.getDecoder().decode(
      "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"
    )
  ), Credentials.from(PasswordQwetu1233)
)
