package buisnesslogic.interactors

import app.keemobile.kotpass.constants.BasicField
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.findEntryBy
import app.keemobile.kotpass.database.modifiers.modifyEntry
import app.keemobile.kotpass.database.modifiers.modifyParentGroup
import app.keemobile.kotpass.models.CustomDataValue
import app.keemobile.kotpass.models.Entry
import app.keemobile.kotpass.models.EntryFields
import app.keemobile.kotpass.models.EntryValue
import buisnesslogic.CUSTOM_DATA_PASSWORD
import buisnesslogic.CUSTOM_DATA_TYPE_KEY
import buisnesslogic.Password
import buisnesslogic.UniqueIdProvider
import buisnesslogic.model.PasswordEntry
import buisnesslogic.model.PasswordEntryData
import java.util.UUID

class KeePassPasswordModelInteractor(private val idProvider: UniqueIdProvider) {
  
  fun getPasswordEntry(database: KeePassDatabase, uuid: String): PasswordEntry {
    return checkNotNull(database.findEntryBy { this.uuid.toString() == uuid })
        .run {
          PasswordEntry(
            id = uuid,
            title = fields.title?.content.orEmpty(),
            username = fields.userName?.content.orEmpty(),
            password = Password.create(fields.password?.content.orEmpty()),
            url = fields.url?.content.orEmpty(),
            notes = fields.notes?.content.orEmpty(),
          )
        }
  }
  
  fun addPassword(
    database: KeePassDatabase,
    passwordEntryData: PasswordEntryData
  ): Pair<KeePassDatabase, String> {
    val uuid = idProvider.generateUniqueId(database)
    val newDatabase = database.modifyParentGroup {
      val entry = Entry(
        uuid = uuid,
        fields = EntryFields.of(
          BasicField.Title.key to EntryValue.Plain(passwordEntryData.title),
          BasicField.UserName.key to EntryValue.Plain(passwordEntryData.username),
          BasicField.Password.key to EntryValue.Encrypted(
            passwordEntryData.password.encryptedValueFiled),
          BasicField.Url.key to EntryValue.Plain(passwordEntryData.url),
          BasicField.Notes.key to EntryValue.Plain(passwordEntryData.notes)
        ),
        customData = mapOf(CUSTOM_DATA_TYPE_KEY to CustomDataValue(CUSTOM_DATA_PASSWORD))
      )
      copy(entries = entries + entry)
    }
    return newDatabase to uuid.toString()
  }
  
  fun editPassword(
    database: KeePassDatabase,
    passwordEntry: PasswordEntry
  ): KeePassDatabase {
    return database.modifyEntry(UUID.fromString(passwordEntry.id)) {
      copy(
        fields = EntryFields.of(
          BasicField.Title.key to EntryValue.Plain(passwordEntry.title),
          BasicField.UserName.key to EntryValue.Plain(passwordEntry.username),
          BasicField.Password.key to EntryValue.Encrypted(
            passwordEntry.password.encryptedValueFiled),
          BasicField.Url.key to EntryValue.Plain(passwordEntry.url),
          BasicField.Notes.key to EntryValue.Plain(passwordEntry.notes)
        )
      )
    }
  }
}
