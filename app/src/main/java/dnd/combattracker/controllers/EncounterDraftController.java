package dnd.combattracker.controllers;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import dnd.combattracker.repository.CombatTrackerContract;
import dnd.combattracker.repository.EncounterCreatureProvider;
import dnd.combattracker.repository.EncounterProvider;

import static dnd.combattracker.repository.CombatTrackerContract.*;

public class EncounterDraftController {
    private ContentResolver contentResolver;

    public EncounterDraftController(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    /**
     * Insert empty encounter draft into the database
     *
     * @return Returns the inserted id
     */
    public long insertEmptyEncounterDraft() {
        ContentValues values = new ContentValues();
        values.put(EncounterEntry.NAME, "");

        return insertEncounterDraft(values);
    }

    public long insertEncounterDraft(ContentValues values) {
        return ControllerUtil.getIdFromUri(contentResolver.insert(EncounterProvider.CONTENT_URI_DRAFT, values));
    }

    public int getDraftCount() {
        String[] projection = {"_id"};
        String selection = "encounterId is null";

        Cursor cursor = contentResolver.query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, null, null);

        int draftCount = cursor.getCount();

        cursor.close();

        return draftCount;
    }

    public long getOldestDraftId() {
        String[] projection = {EncounterDraftEntry._ID};
        String selection = "encounterId is null";

        Cursor cursor = contentResolver.query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, null, "_id ASC");

        long draftId;

        cursor.moveToFirst();
        draftId = cursor.getLong(cursor.getColumnIndex(EncounterDraftEntry._ID));

        cursor.close();

        return draftId;
    }

    public long getOldestDraftIdByEncounterId(long encounterId) {
        String[] projection = {EncounterDraftEntry._ID};
        String selection = "encounterId = ?";
        String[] selectionArgs = {String.valueOf(encounterId)};

        Cursor cursor = contentResolver.query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, selectionArgs, "_id ASC");

        long draftId;

        cursor.moveToFirst();
        draftId = cursor.getLong(cursor.getColumnIndex(EncounterDraftEntry._ID));

        cursor.close();

        return draftId;
    }

    public int deleteOldestDraft() {
        String selectionClause = "_id = ?";
        String[] selectionArgs = {String.valueOf(getOldestDraftId())};

        return contentResolver.delete(EncounterProvider.CONTENT_URI_DRAFT, selectionClause, selectionArgs);
    }

    public int deleteOldestDraftForEncounterId(long encounterId) {
        String selectionClause = "encounterId = ?";
        String[] selectionArgs = {String.valueOf(getOldestDraftIdByEncounterId(encounterId))};

        return contentResolver.delete(EncounterProvider.CONTENT_URI_DRAFT, selectionClause, selectionArgs);
    }

    public int getDraftCountById(long encounterId) {
        String[] projection = {"_id"};
        String selection = "encounterId = ?";
        String[] selectionArgs = {String.valueOf(encounterId)};

        Cursor cursor = contentResolver.query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, selectionArgs, null);

        int draftCount = cursor.getCount();

        cursor.close();

        return draftCount;
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

        long newEncounterId = ControllerUtil.getIdFromUri(contentResolver.insert(EncounterProvider.CONTENT_URI_DRAFT, data));

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

        cursor.close();

        contentResolver.bulkInsert(EncounterCreatureProvider.CONTENT_URI_DRAFT, data);
    }

    public int deleteDraftById(long encounterDraftId) {
        String selectionClause = "_id = ?";
        String[] selectionArgs = {String.valueOf(encounterDraftId)};

        return contentResolver.delete(EncounterProvider.CONTENT_URI_DRAFT, selectionClause, selectionArgs);
    }

    public int deleteDraftByEncounterId(long encounterId) {
        String selectionClause = EncounterDraftEntry.ENCOUNTER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(encounterId)};

        return contentResolver.delete(EncounterProvider.CONTENT_URI_DRAFT, selectionClause, selectionArgs);
    }

    public int deleteDraftWithoutEncounterId() {
        String selectionClause = EncounterDraftEntry.ENCOUNTER_ID + " IS NULL";

        return contentResolver.delete(EncounterProvider.CONTENT_URI_DRAFT, selectionClause, null);
    }

    /**
     * Updates the draft of the encounter
     *
     * @param encounterDraftId
     * @param values
     * @return Returns true on success, and false on failure
     */
    public boolean updateEncounterDraftById(long encounterDraftId, ContentValues values) {
        String selectionClause = "_id = ?";
        String[] selectionArgs = {String.valueOf(encounterDraftId)};
        return contentResolver.update(EncounterProvider.CONTENT_URI_DRAFT, values, selectionClause, selectionArgs) != -1;
    }

    /**
     * @param encounterDraftId
     * @return Returns a cursor containing the encounterDraft
     */
    public Cursor getEncounterDraftFromId(long encounterDraftId) {
        String[] projection = {"name"};
        String selection = "_id = ?";
        String[] selectionArgs = {String.valueOf(encounterDraftId)};

        return contentResolver.query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, selectionArgs, null);
    }
}
