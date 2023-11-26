package com.arsvechkarev.vault.test.core.data

import app.keemobile.kotpass.constants.BasicField.Notes
import app.keemobile.kotpass.constants.BasicField.Password
import app.keemobile.kotpass.constants.BasicField.Title
import app.keemobile.kotpass.constants.BasicField.Url
import app.keemobile.kotpass.constants.BasicField.UserName
import app.keemobile.kotpass.cryptography.EncryptedValue.Companion.fromString
import app.keemobile.kotpass.database.Credentials
import app.keemobile.kotpass.database.KeePassDatabase
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
import java.util.UUID

object Databases {
  
  val PasswordQwetu1233 = domain.Password.create("qwetu1233")
  
  val Empty = KeePassDatabase.Ver4x.create(
    rootName = CommonConstants.DEFAULT_DATABASE_NAME,
    meta = Meta(),
    credentials = Credentials.from(PasswordQwetu1233)
  )
  
  val TwoPasswordsAndNote = KeePassDatabase.Ver4x.create(
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
            Password.key to Encrypted(fromString("F/<1#E(J=\\51=k;")),
            Url.key to Plain(""),
            Notes.key to Plain("")
          ),
          customData = mapOf(
            CUSTOM_DATA_TYPE_KEY to CustomDataValue(CUSTOM_DATA_PASSWORD),
            CUSTOM_DATA_FAVORITE_KEY to CustomDataValue("false"),
          )
        ),
        Entry(
          uuid = UUID.fromString("fe674044-8e7b-4ba2-b316-20bf841d3bcd"),
          fields = EntryFields.of(
            Title.key to Plain("test.com"),
            UserName.key to Plain("abcd"),
            Password.key to Encrypted(fromString("q3z;ob15/*8GK>Ed")),
            Url.key to Plain(""),
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
            Title.key to Plain("my title"),
            Notes.key to Plain("super secret content")
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
  
  val OnePassword = TwoPasswordsAndNote.modifyParentGroup {
    copy(entries = entries.filter { it.uuid.toString() == "fe674044-8e7b-4ba2-b316-20bf841d3bcd" })
  }
  
  val TwoPasswords = TwoPasswordsAndNote.modifyParentGroup {
    copy(entries = entries.filter { it.uuid.toString() != "3d2f85f1-490a-4290-8d66-7de9f9769858" })
  }
  
  val OnePasswordAndNote = TwoPasswordsAndNote.modifyParentGroup {
    copy(entries = entries.filter { it.uuid.toString() != "fe674044-8e7b-4ba2-b316-20bf841d3bcd" })
  }
  
  val EncodedDatabaseFromKeePass = "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"
}
