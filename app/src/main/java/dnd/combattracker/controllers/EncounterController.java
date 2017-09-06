package dnd.combattracker.controllers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

import dnd.combattracker.repository.EncounterProvider;

import static dnd.combattracker.repository.CombatTrackerContract.EncounterEntry;

public class EncounterController {

    private ContentResolver contentResolver;

    public EncounterController(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    /**
     * Copy the draft over to the real table
     *
     * @param encounterDraft
     * @return
     */
    public long insertEncounterFromDraft(Cursor encounterDraft) {
        ContentValues data = new ContentValues();

        while (encounterDraft.moveToNext()) {
            data.put("name", encounterDraft.getString(encounterDraft.getColumnIndex("name")));
        }

        encounterDraft.close();

        return ControllerUtil.getIdFromUri(contentResolver.insert(EncounterProvider.CONTENT_URI, data));
    }

    /**
     * Delete multiple encounters
     *
     * @param encounterIds
     * @return Returns the amount of deleted rows
     */
    public int deleteEncounters(List<String> encounterIds) {
        String questionMarks = "";

        for (String ignored : encounterIds) {
            questionMarks += "?, ";
        }
        questionMarks = questionMarks.substring(0, questionMarks.length() - 2);

        return contentResolver.delete(EncounterProvider.CONTENT_URI, EncounterEntry._ID + " IN (" + questionMarks + ")", encounterIds.toArray(new String[0]));
    }

    /**
     * Update the official encounter from the draft
     *
     * @param encounterId
     * @param encounterDraft
     * @return Return true or false
     */
    public boolean updateEncounterFromDraft(long encounterId, Cursor encounterDraft) {
        String selectionClause = "_id = ?";
        String[] selectionArgs = {String.valueOf(encounterId)};

        ContentValues values = new ContentValues();

        while (encounterDraft.moveToNext()) {
            values.put(EncounterEntry.NAME, encounterDraft.getString(encounterDraft.getColumnIndex(EncounterEntry.NAME)));
        }

        encounterDraft.close();

        return contentResolver.update(EncounterProvider.CONTENT_URI, values, selectionClause, selectionArgs) != -1;
    }
}
