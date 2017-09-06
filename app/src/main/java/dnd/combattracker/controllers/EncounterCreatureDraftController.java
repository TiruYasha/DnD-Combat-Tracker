package dnd.combattracker.controllers;

import android.content.ContentResolver;
import android.content.ContentValues;

import dnd.combattracker.repository.CombatTrackerContract;
import dnd.combattracker.repository.EncounterCreatureProvider;

public class EncounterCreatureDraftController {
    private ContentResolver contentResolver;

    public EncounterCreatureDraftController(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public long insertCreatureForEncounter(long encounterId, long creatureId) {
        ContentValues values = new ContentValues();
        values.put(CombatTrackerContract.EncounterCreatureDraftEntry.ENCOUNTER_ID, encounterId);
        values.put(CombatTrackerContract.EncounterCreatureDraftEntry.CREATURE_ID, creatureId);

        return ControllerUtil.getIdFromUri(contentResolver.insert(EncounterCreatureProvider.CONTENT_URI_DRAFT, values));
    }
}
