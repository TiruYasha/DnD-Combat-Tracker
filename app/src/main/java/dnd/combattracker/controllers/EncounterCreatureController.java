package dnd.combattracker.controllers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import dnd.combattracker.repository.EncounterCreatureProvider;

import static dnd.combattracker.repository.CombatTrackerContract.EncounterCreatureDraftEntry;
import static dnd.combattracker.repository.CombatTrackerContract.EncounterCreatureEntry;

public class EncounterCreatureController {
    private ContentResolver contentResolver;

    public EncounterCreatureController(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void insertCreaturesFromDraftId(long encounterId, long encounterDraftId) {
        String selection = EncounterCreatureDraftEntry.ENCOUNTER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(encounterDraftId)};

        Cursor encounterCreatures = contentResolver.query(EncounterCreatureProvider.CONTENT_URI_DRAFT, null, selection, selectionArgs, null);

        ContentValues[] values = new ContentValues[encounterCreatures.getCount()];

        int i = 0;
        while(encounterCreatures.moveToNext()){
            ContentValues data = new ContentValues();
            data.put(EncounterCreatureEntry.CREATURE_ID, encounterCreatures.getLong(encounterCreatures.getColumnIndex(EncounterCreatureDraftEntry.CREATURE_ID)));
            data.put(EncounterCreatureEntry.ENCOUNTER_ID, encounterId);

            values[i] = data;
            i++;
        }

        encounterCreatures.close();

        String selectionOfficial = EncounterCreatureEntry.ENCOUNTER_ID + " = ?";
        String[] selectionArgsOfficial = {String.valueOf(encounterId)};

        contentResolver.delete(EncounterCreatureProvider.CONTENT_URI, selectionOfficial, selectionArgsOfficial);
        contentResolver.bulkInsert(EncounterCreatureProvider.CONTENT_URI, values);
        contentResolver.delete(EncounterCreatureProvider.CONTENT_URI_DRAFT, selection, selectionArgs);
    }
}
