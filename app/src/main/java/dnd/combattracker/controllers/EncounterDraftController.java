package dnd.combattracker.controllers;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import dnd.combattracker.repository.CombatTrackerContract;
import dnd.combattracker.repository.EncounterCreatureProvider;
import dnd.combattracker.repository.EncounterProvider;

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
        values.put(CombatTrackerContract.EncounterEntry.NAME, "");

        return insertEncounterDraft(values);
    }

    public long insertEncounterDraft(ContentValues values) {
        return ControllerUtil.getIdFromUri(contentResolver.insert(EncounterProvider.CONTENT_URI_DRAFT, values));
    }

    public int getDraftCount() {
        String[] projection = {"_id"};
        String selection = "encounterId is null";

        Cursor cursor = contentResolver.query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, null, null);

        return cursor.getCount();
    }

    public long getOldestDraftId() {
        String[] projection = {CombatTrackerContract.EncounterDraftEntry._ID};
        String selection = "encounterId is null";

        Cursor cursor = contentResolver.query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, null, "_id ASC");

        long draftId;

        cursor.moveToFirst();
        draftId = cursor.getLong(cursor.getColumnIndex(CombatTrackerContract.EncounterDraftEntry._ID));

        return draftId;
    }

    public long getOldestDraftIdByEncounterId(long encounterId) {
        String[] projection = {CombatTrackerContract.EncounterDraftEntry._ID};
        String selection = "encounterId = ?";
        String[] selectionArgs = {String.valueOf(encounterId)};

        Cursor cursor = contentResolver.query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, selectionArgs, "_id ASC");

        long draftId;

        cursor.moveToFirst();
        draftId = cursor.getLong(cursor.getColumnIndex(CombatTrackerContract.EncounterDraftEntry._ID));

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

        return cursor.getCount();
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
        String[] projection = {CombatTrackerContract.EncounterCreatureDraftEntry.CREATURE_ID};
        String selection = CombatTrackerContract.EncounterCreatureDraftEntry.ENCOUNTER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(encounterId)};

        Cursor cursor = contentResolver.query(EncounterCreatureProvider.CONTENT_URI, projection, selection, selectionArgs, null);

        ContentValues[] data = new ContentValues[cursor.getCount()];

        int i = 0;
        while (cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put(CombatTrackerContract.EncounterCreatureDraftEntry.CREATURE_ID, cursor.getLong(cursor.getColumnIndex(CombatTrackerContract.EncounterCreatureDraftEntry.CREATURE_ID)));
            values.put(CombatTrackerContract.EncounterCreatureDraftEntry.ENCOUNTER_ID, draftEncounterId);
            data[i] = values;
            i++;
        }

        contentResolver.bulkInsert(EncounterCreatureProvider.CONTENT_URI_DRAFT, data);
    }

    public int deleteDraftById(long encounterDraftId) {
        String selectionClause = "_id = ?";
        String[] selectionArgs = {String.valueOf(encounterDraftId)};

        return contentResolver.delete(EncounterProvider.CONTENT_URI_DRAFT, selectionClause, selectionArgs);
    }
}
