package dnd.combattracker.controllers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;

import dnd.combattracker.repository.EncounterCreatureProvider;
import dnd.combattracker.repository.EncounterProvider;

import static dnd.combattracker.repository.CombatTrackerContract.*;

public class EncounterController {

    private ContentResolver contentResolver;

    public EncounterController(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    /**
     * Insert encounter draft into the database
     *
     * @param values values to insert
     * @return Returns the inserted id
     */
    public long insertEncounterDraft(ContentValues values) {
        return getIdFromUri(contentResolver.insert(EncounterProvider.CONTENT_URI_DRAFT, values));
    }

    /**
     * Copy draft to the real table
     *
     * @param draftId draftId
     * @return return inserted id
     */
    public long insertEncounterFromDraft(long draftId) {
        Cursor cursor = getEncounterDraftFromId(draftId);

        ContentValues data = new ContentValues();

        while (cursor.moveToNext()) {
            data.put("name", cursor.getString(cursor.getColumnIndex("name")));
        }

        cursor.close();

        long encounterId = getIdFromUri(contentResolver.insert(EncounterProvider.CONTENT_URI, data));

        insertCreaturesFromDraft(encounterId, draftId);

        return encounterId;
    }

    private void insertCreaturesFromDraft(long encounterId, long encounterDraftId) {
        String selection = EncounterCreatureDraftEntry.ENCOUNTER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(encounterDraftId)};

        Cursor cursor = contentResolver.query(EncounterCreatureProvider.CONTENT_URI_DRAFT, null, selection, selectionArgs, null);

        ContentValues[] values = new ContentValues[cursor.getCount()];

        int i = 0;
        while(cursor.moveToNext()){
            ContentValues data = new ContentValues();
            data.put(EncounterCreatureEntry.CREATURE_ID, cursor.getLong(cursor.getColumnIndex(EncounterCreatureDraftEntry.CREATURE_ID)));
            data.put(EncounterCreatureEntry.ENCOUNTER_ID, encounterId);

            values[i] = data;
            i++;
        }

        String selectionOfficial = EncounterCreatureEntry.ENCOUNTER_ID + " = ?";
        String[] selectionArgsOfficial = {String.valueOf(encounterId)};

        contentResolver.delete(EncounterCreatureProvider.CONTENT_URI, selectionOfficial, selectionArgsOfficial);
        contentResolver.bulkInsert(EncounterCreatureProvider.CONTENT_URI, values);
        contentResolver.delete(EncounterCreatureProvider.CONTENT_URI_DRAFT, selection, selectionArgs);

    }

    /**
     * Create draft copy of the official encounter
     *
     * @param encounterId
     * @return The draftId
     */
    public long insertDraftFromEncounter(long encounterId) {
        String[] projection = {"name"};
        String selection = "_id = ?";
        String[] selectionArgs = {String.valueOf(encounterId)};

        Cursor cursor = contentResolver.query(EncounterProvider.CONTENT_URI, projection, selection, selectionArgs, null);

        ContentValues data = new ContentValues();

        while (cursor.moveToNext()) {
            data.put("name", cursor.getString(cursor.getColumnIndex("name")));
            data.put("encounterId", encounterId);
        }

        cursor.close();

        long newEncounterId = getIdFromUri(contentResolver.insert(EncounterProvider.CONTENT_URI_DRAFT, data));

        insertDraftCreaturesFromEncounter(encounterId, newEncounterId);

        return newEncounterId;
    }

    private void insertDraftCreaturesFromEncounter(long encounterId, long draftEncounterId) {
        String[] projection = {EncounterCreatureDraftEntry.CREATURE_ID};
        String selection = EncounterCreatureDraftEntry.ENCOUNTER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(encounterId)};

        Cursor cursor = contentResolver.query(EncounterCreatureProvider.CONTENT_URI, projection, selection, selectionArgs, null);

        ContentValues[] data = new ContentValues[cursor.getCount()];

        int i = 0;
        while (cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put(EncounterCreatureDraftEntry.CREATURE_ID, cursor.getLong(cursor.getColumnIndex(EncounterCreatureDraftEntry.CREATURE_ID)));
            values.put(EncounterCreatureDraftEntry.ENCOUNTER_ID, draftEncounterId);
            data[i] = values;
            i++;
        }

        contentResolver.bulkInsert(EncounterCreatureProvider.CONTENT_URI_DRAFT, data);
    }

    /**
     * Delete multiple encounters
     *
     * @param encounterIds
     * @return Returns the amount of deleted rows
     */
    public int deleteEncounters(List<String> encounterIds) {
        String questionMarks = "";

        for (String id : encounterIds) {
            questionMarks += "?, ";
        }
        questionMarks = questionMarks.substring(0, questionMarks.length() - 2);

        return contentResolver.delete(EncounterProvider.CONTENT_URI, EncounterEntry._ID + " IN (" + questionMarks + ")", encounterIds.toArray(new String[0]));
    }

    /**
     * Updates the draft of the encounter
     *
     * @param id
     * @param values
     * @return Returns true on success, and false on failure
     */
    public boolean updateEncounterDraftById(long id, ContentValues values) {
        String selectionClause = "_id = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return contentResolver.update(EncounterProvider.CONTENT_URI_DRAFT, values, selectionClause, selectionArgs) != -1;
    }

    /**
     * @param id
     * @return Returns a cursor
     */
    public Cursor getEncounterDraftFromId(long id) {
        String[] projection = {"name"};
        String selection = "_id = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return contentResolver.query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, selectionArgs, null);
    }

    /**
     * Update the official encounter from the draft
     *
     * @param encounterId
     * @param encounterDraftId
     * @return Return true or false
     */
    public boolean updateEncounterFromDraft(long encounterId, long encounterDraftId) {
        Cursor cursor = getEncounterDraftFromId(encounterDraftId);

        String selectionClause = "_id = ?";
        String[] selectionArgs = {String.valueOf(encounterId)};

        ContentValues values = new ContentValues();

        while (cursor.moveToNext()) {
            values.put(EncounterEntry.NAME, cursor.getString(cursor.getColumnIndex(EncounterEntry.NAME)));
        }

        cursor.close();

        insertCreaturesFromDraft(encounterId, encounterDraftId);

        return contentResolver.update(EncounterProvider.CONTENT_URI, values, selectionClause, selectionArgs) != -1;
    }

    public long getDraftIdByEncounterId(long encounterId) {
        String[] projection = {"_id"};
        String selection = "encounterId = ?";
        String[] selectionArgs = {String.valueOf(encounterId)};

        Cursor cursor = contentResolver.query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, selectionArgs, null);

        long id = -1;

        while (cursor.moveToNext()) {
            id = cursor.getLong(cursor.getColumnIndex("_id"));
        }

        return id;
    }

    public long getDraftIdWithoutEncounterId() {
        String[] projection = {"_id"};
        String selection = "encounterId is null";

        Cursor cursor = contentResolver.query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, null, null);

        long draftId = -1;

        while (cursor.moveToNext()) {
            draftId = cursor.getLong(cursor.getColumnIndex("_id"));
        }

        return draftId;
    }

    public long addCreatureToEncounterDraft(long encounterId, long creatureId) {
        ContentValues values = new ContentValues();
        values.put(EncounterCreatureDraftEntry.ENCOUNTER_ID, encounterId);
        values.put(EncounterCreatureDraftEntry.CREATURE_ID, creatureId);

        return getIdFromUri(contentResolver.insert(EncounterCreatureProvider.CONTENT_URI_DRAFT, values));
    }

    private long getIdFromUri(Uri result) {
        return Long.parseLong(result.getLastPathSegment());
    }
}
