package domain.interactors

import app.keemobile.kotpass.constants.BasicField
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.getEntryBy
import app.keemobile.kotpass.database.modifiers.modifyEntry
import app.keemobile.kotpass.database.modifiers.modifyParentGroup
import app.keemobile.kotpass.models.CustomDataValue
import app.keemobile.kotpass.models.Entry
import app.keemobile.kotpass.models.EntryFields
import app.keemobile.kotpass.models.EntryValue
import domain.CUSTOM_DATA_FAVORITE_KEY
import domain.CUSTOM_DATA_PASSWORD
import domain.CUSTOM_DATA_TYPE_KEY
import domain.InstantProvider
import domain.Password
import domain.UniqueIdProvider
import domain.model.PasswordEntry
import domain.model.PasswordEntryData
import java.util.UUID

class KeePassPasswordEntryInteractor(
  private val idProvider: UniqueIdProvider,
  private val instantProvider: InstantProvider
) {
  
  fun getPasswordEntry(database: KeePassDatabase, uuid: String): PasswordEntry {
    val entry = database.getEntryBy { this.uuid.toString() == uuid }
    return checkNotNull(entry).asPasswordEntry(uuid)
  }
  
  fun addPassword(
    database: KeePassDatabase,
    passwordEntryData: PasswordEntryData
  ): Pair<KeePassDatabase, PasswordEntry> {
    val uuid = idProvider.generateUniqueId(database)
    val entry = Entry(
      uuid = uuid,
      fields = EntryFields.of(
        BasicField.Title.key to EntryValue.Plain(passwordEntryData.title),
        BasicField.UserName.key to EntryValue.Plain(passwordEntryData.username),
        BasicField.Password.key to EntryValue.Encrypted(
          passwordEntryData.password.encryptedValueField),
        BasicField.Url.key to EntryValue.Plain(passwordEntryData.url),
        BasicField.Notes.key to EntryValue.Plain(passwordEntryData.notes)
      ),
      customData = mapOf(
        CUSTOM_DATA_TYPE_KEY to CustomDataValue(CUSTOM_DATA_PASSWORD),
        CUSTOM_DATA_FAVORITE_KEY to passwordEntryData.isFavorite.toValue(instantProvider.now()),
      )
    )
    val newDatabase = database.modifyParentGroup {
      copy(entries = entries + entry)
    }
    return newDatabase to entry.asPasswordEntry(uuid.toString())
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
            passwordEntry.password.encryptedValueField),
          BasicField.Url.key to EntryValue.Plain(passwordEntry.url),
          BasicField.Notes.key to EntryValue.Plain(passwordEntry.notes)
        ),
        customData = HashMap(customData).apply {
          put(CUSTOM_DATA_FAVORITE_KEY, passwordEntry.isFavorite.toValue(instantProvider.now()))
        }
      )
    }
  }
  
  private fun Entry.asPasswordEntry(uuid: String): PasswordEntry {
    return PasswordEntry(
      id = uuid,
      title = fields.title?.content.orEmpty(),
      username = fields.userName?.content.orEmpty(),
      password = Password.create(fields.password?.content.orEmpty()),
      url = fields.url?.content.orEmpty(),
      notes = fields.notes?.content.orEmpty(),
      isFavorite = customData[CUSTOM_DATA_FAVORITE_KEY]?.asBoolean() ?: false
    )
  }
}
