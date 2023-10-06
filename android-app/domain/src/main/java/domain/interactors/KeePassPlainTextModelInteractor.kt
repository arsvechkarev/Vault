package domain.interactors

import app.keemobile.kotpass.constants.BasicField
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.findEntryBy
import app.keemobile.kotpass.database.modifiers.modifyEntry
import app.keemobile.kotpass.database.modifiers.modifyParentGroup
import app.keemobile.kotpass.models.CustomDataValue
import app.keemobile.kotpass.models.Entry
import app.keemobile.kotpass.models.EntryFields
import app.keemobile.kotpass.models.EntryValue
import domain.CUSTOM_DATA_FAVORITE_KEY
import domain.CUSTOM_DATA_PLAIN_TEXT
import domain.CUSTOM_DATA_TYPE_KEY
import domain.InstantProvider
import domain.UniqueIdProvider
import domain.model.PlainTextEntry
import domain.model.PlainTextEntryData
import java.util.UUID

class KeePassPlainTextModelInteractor(
  private val idProvider: UniqueIdProvider,
  private val instantProvider: InstantProvider
) {
  
  fun getPlainTextEntry(database: KeePassDatabase, uuid: String): PlainTextEntry {
    return checkNotNull(database.findEntryBy { this.uuid.toString() == uuid })
        .run {
          PlainTextEntry(
            id = uuid,
            title = fields.title?.content.orEmpty(),
            text = fields.notes?.content.orEmpty(),
            isFavorite = customData[CUSTOM_DATA_FAVORITE_KEY]?.asBoolean() ?: false
          )
        }
  }
  
  fun addPlainText(
    database: KeePassDatabase,
    plainTextEntryData: PlainTextEntryData
  ): Pair<KeePassDatabase, String> {
    val uuid = idProvider.generateUniqueId(database)
    val newDatabase = database.modifyParentGroup {
      val entry = Entry(
        uuid = uuid,
        fields = EntryFields.of(
          BasicField.Title.key to EntryValue.Plain(plainTextEntryData.title),
          BasicField.Notes.key to EntryValue.Plain(plainTextEntryData.text)
        ),
        customData = mapOf(
          CUSTOM_DATA_TYPE_KEY to CustomDataValue(CUSTOM_DATA_PLAIN_TEXT),
          CUSTOM_DATA_FAVORITE_KEY to plainTextEntryData.isFavorite.toValue(instantProvider.now()),
        )
      )
      copy(entries = entries + entry)
    }
    return newDatabase to uuid.toString()
  }
  
  fun editPlainText(
    database: KeePassDatabase,
    plainTextEntry: PlainTextEntry
  ): KeePassDatabase {
    return database.modifyEntry(UUID.fromString(plainTextEntry.id)) {
      copy(
        fields = EntryFields.of(
          BasicField.Title.key to EntryValue.Plain(plainTextEntry.title),
          BasicField.Notes.key to EntryValue.Plain(plainTextEntry.text)
        ),
        customData = HashMap(customData).apply {
          put(CUSTOM_DATA_FAVORITE_KEY, plainTextEntry.isFavorite.toValue(instantProvider.now()))
        }
      )
    }
  }
}
