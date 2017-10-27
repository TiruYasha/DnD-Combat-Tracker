package tiru.initiative.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import tiru.initiative.repository.InitiativeTrackerContract.EncounterDraftEntry
import tiru.initiative.repository.InitiativeTrackerContract.EncounterEntry
import android.database.sqlite.SQLiteQueryBuilder
import android.content.UriMatcher
import tiru.initiative.repository.InitiativeTrackerDbHelper


class EncounterProvider : ContentProvider() {

    private lateinit var dbHelper: InitiativeTrackerDbHelper

    companion object {
        private val ENCOUNTER = 10
        private val ENCOUNTER_DRAFT = 11

        private val AUTHORITY = "tiru.initiative.providers.EncounterProvider"

        private val BASE_PATH = "encounters"
        private val BASE_PATH_DRAFT = "encounters/draft"

        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$BASE_PATH")!!
        val CONTENT_URI_DRAFT = Uri.parse("content://$AUTHORITY/$BASE_PATH_DRAFT")!!
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, ENCOUNTER)
        uriMatcher.addURI(AUTHORITY, BASE_PATH_DRAFT, ENCOUNTER_DRAFT)
    }

    override fun onCreate(): Boolean {
        dbHelper = InitiativeTrackerDbHelper(
                context
        )

        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val queryBuilder = SQLiteQueryBuilder()

        when (uriMatcher.match(uri)) {
            ENCOUNTER -> queryBuilder.tables = EncounterEntry.TABLE_NAME
            ENCOUNTER_DRAFT -> queryBuilder.tables = EncounterDraftEntry.TABLE_NAME
            else -> throw IllegalArgumentException("Unknown Uri: $uri")
        }

        val db = dbHelper.readableDatabase
        val cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder, null)

        cursor.setNotificationUri(context.contentResolver, uri)

        return cursor
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        var id: Long
        var uriToReturn = ""

        dbHelper.writableDatabase.use { db ->
            when (uriMatcher.match(uri)) {
                ENCOUNTER -> {
                    id = db.insert(EncounterEntry.TABLE_NAME, null, values)
                    uriToReturn = "$BASE_PATH/$id"
                }
                ENCOUNTER_DRAFT -> {
                    id = db.insert(EncounterDraftEntry.TABLE_NAME, null, values)
                    uriToReturn = "$BASE_PATH_DRAFT/$id"
                }
                else -> throw IllegalArgumentException("Unknown Uri: $uri")
            }
        }

        context.contentResolver.notifyChange(uri, null)

        return Uri.parse(uriToReturn)
    }

    override fun update(uri: Uri?, values: ContentValues?, where: String?, whereArgs: Array<out String>?): Int {
        val db = dbHelper.writableDatabase
        val success: Int

        when(uriMatcher.match(uri)){
            ENCOUNTER_DRAFT -> success = db.update(EncounterDraftEntry.TABLE_NAME, values, where, whereArgs)
            else -> throw IllegalArgumentException("Unknown Uri: $uri")
        }

        context.contentResolver.notifyChange(uri, null)

        return success
    }

    override fun delete(uri: Uri?, selection: String?, args: Array<out String>?): Int {
        val db = dbHelper.writableDatabase
        val numberOfRows: Int

        when(uriMatcher.match(uri)){
            ENCOUNTER_DRAFT -> numberOfRows = db.delete(EncounterDraftEntry.TABLE_NAME, selection, args)
            else -> throw IllegalArgumentException("Unknown Uri: $uri")
        }

        context.contentResolver.notifyChange(uri, null)
        return numberOfRows
    }

    override fun getType(p0: Uri?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}