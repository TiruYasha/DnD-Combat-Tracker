package tiru.initiative.services

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import tiru.initiative.models.Encounter
import tiru.initiative.providers.EncounterProvider
import tiru.initiative.repository.InitiativeTrackerContract
import tiru.initiative.repository.InitiativeTrackerContract.EncounterDraftEntry

class EncounterService(private val contentResolver: ContentResolver) {

    fun setupDraft(id: Long?): Long {
        return if (id == null) {
            createEmptyDraft()
        } else {
            createDraftFromEncounter(id)
        }
    }

    fun getDraftCountByEncounterId(id: Long?): Long {
        val projection = arrayOf("count(*) AS count")
        val selection = if (id == null) "${EncounterDraftEntry.ENCOUNTER_ID} is null" else "${EncounterDraftEntry.ENCOUNTER_ID} = ?"
        val selectionArgs: Array<String>? = if (id == null) null else arrayOf("" + id)

        contentResolver.query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, selectionArgs, null).use { cursor ->
            cursor.moveToFirst()
            return cursor.getLong(0)
        }
    }

    fun updateDraftName(draftId: Long, name: String) {
        val values = ContentValues()
        values.put(EncounterDraftEntry.NAME, name)

        val whereClause = "_id = ?"
        val whereArgs = arrayOf(draftId.toString())

        contentResolver.update(EncounterProvider.CONTENT_URI_DRAFT, values, whereClause, whereArgs)
    }

    fun saveEncounterFromDraft(id: Long?, draftId: Long) {
        if (id == null) {
            createNewEncounterFromDraft(draftId)
        } else {
            //TODO update existing encounter
        }
    }

    fun restoreDraft(id: Long?): Encounter {
        val draftId = getOldestDraftIdByEncounterId(id)

        deleteNotUsedDrafts(id, draftId)

        getDraftById(draftId).use { draft ->
            draft.moveToFirst()

            return Encounter(draftId, draft.getString(draft.getColumnIndex(EncounterDraftEntry.NAME)))
        }
    }

    private fun deleteNotUsedDrafts(id: Long?, draftId: Long) {
        val selectionClause = "_id != ? AND " + if (id == null) "${EncounterDraftEntry.ENCOUNTER_ID} is null" else "${EncounterDraftEntry.ENCOUNTER_ID} = ?"
        val selectionArgs = if (id == null) {
            arrayOf(draftId.toString())
        } else {
            arrayOf(draftId.toString(), id.toString())
        }

        contentResolver.delete(EncounterProvider.CONTENT_URI_DRAFT, selectionClause, selectionArgs)
    }

    private fun createNewEncounterFromDraft(draftId: Long) {
        val values = ContentValues()

        getDraftById(draftId).use { cursor ->
            cursor.moveToFirst()
            values.put(InitiativeTrackerContract.EncounterEntry.NAME, cursor.getString(0))
        }

        contentResolver.insert(EncounterProvider.CONTENT_URI, values)
    }

    private fun getDraftById(draftId: Long): Cursor {
        val projection = arrayOf(EncounterDraftEntry.NAME)
        val selection = "_id = ?"
        val selectionArgs = arrayOf(draftId.toString())

        return contentResolver.query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, selectionArgs, null)
    }

    private fun createDraftFromEncounter(id: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Create draft without id.
     * If there are already 2 drafts, delete the oldest
     */
    private fun createEmptyDraft(): Long {
        val draftCount = getDraftCountByEncounterId(null)

        if (draftCount > 1) {
            deleteOldestDraft(null)
        }

        val values = ContentValues()
        values.put(EncounterDraftEntry.NAME, "")

        return insertDraft(values)
    }

    private fun deleteOldestDraft(id: Long?) {
        val draftId = getOldestDraftIdByEncounterId(id)

        val selectionClause = "_id = ?"
        val selectionArgs = arrayOf(draftId.toString())

        contentResolver.delete(EncounterProvider.CONTENT_URI_DRAFT, selectionClause, selectionArgs)
    }

    private fun getNewestDraftIdByEncounterId(id: Long?): Long {
        val projection = arrayOf("_id")
        val selection = if (id == null) "${EncounterDraftEntry.ENCOUNTER_ID} is null" else "${EncounterDraftEntry.ENCOUNTER_ID} = ?"
        val selectionArgs: Array<String>? = if (id == null) null else arrayOf("" + id)

        contentResolver.query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, selectionArgs, "_id DESC").use { cursor ->
            cursor.moveToFirst()
            return cursor.getLong(0)
        }
    }

    private fun getOldestDraftIdByEncounterId(id: Long?): Long {
        val projection = arrayOf("_id")
        val selection = if (id == null) "${EncounterDraftEntry.ENCOUNTER_ID} is null" else "${EncounterDraftEntry.ENCOUNTER_ID} = ?"
        val selectionArgs: Array<String>? = if (id == null) null else arrayOf("" + id)

        contentResolver.query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, selectionArgs, "_id ASC").use { cursor ->
            cursor.moveToFirst()
            return cursor.getLong(0)
        }
    }

    private fun insertDraft(values: ContentValues): Long {
        val uri = contentResolver.insert(EncounterProvider.CONTENT_URI_DRAFT, values)

        return getIdFromUri(uri)
    }

    private fun getIdFromUri(uri: Uri?): Long {
        return uri!!.lastPathSegment!!.toLong()
    }


}