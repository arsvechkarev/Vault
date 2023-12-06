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
import domain.CUSTOM_DATA_NOTE
import domain.CUSTOM_DATA_TYPE_KEY
import domain.InstantProvider
import domain.UniqueIdProvider
import domain.model.NoteEntry
import domain.model.NoteEntryData
import java.util.UUID

class KeePassNoteEntryInteractor(
  private val idProvider: UniqueIdProvider,
  private val instantProvider: InstantProvider
) {
  
  fun getNoteEntry(database: KeePassDatabase, uuid: String): NoteEntry {
    return checkNotNull(database.getEntryBy { this.uuid.toString() == uuid })
        .run {
          NoteEntry(
            id = uuid,
            title = fields.title?.content.orEmpty(),
            text = fields.notes?.content.orEmpty(),
            isFavorite = customData[CUSTOM_DATA_FAVORITE_KEY]?.asBoolean() ?: false
          )
        }
  }
  
  fun addNote(
    database: KeePassDatabase,
    noteEntryData: NoteEntryData
  ): Pair<KeePassDatabase, String> {
    val uuid = idProvider.generateUniqueId(database)
    val newDatabase = database.modifyParentGroup {
      val entry = Entry(
        uuid = uuid,
        fields = EntryFields.of(
          BasicField.Title.key to EntryValue.Plain(noteEntryData.title),
          BasicField.Notes.key to EntryValue.Plain(noteEntryData.text)
        ),
        customData = mapOf(
          CUSTOM_DATA_TYPE_KEY to CustomDataValue(CUSTOM_DATA_NOTE),
          CUSTOM_DATA_FAVORITE_KEY to noteEntryData.isFavorite.toValue(instantProvider.now()),
        )
      )
      copy(entries = entries + entry)
    }
    return newDatabase to uuid.toString()
  }
  
  fun editNote(
    database: KeePassDatabase,
    noteEntry: NoteEntry
  ): KeePassDatabase {
    return database.modifyEntry(UUID.fromString(noteEntry.id)) {
      copy(
        fields = EntryFields.of(
          BasicField.Title.key to EntryValue.Plain(noteEntry.title),
          BasicField.Notes.key to EntryValue.Plain(noteEntry.text)
        ),
        customData = HashMap(customData).apply {
          put(CUSTOM_DATA_FAVORITE_KEY, noteEntry.isFavorite.toValue(instantProvider.now()))
        }
      )
    }
  }
}
