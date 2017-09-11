package dnd.combattracker.controllers;

import android.content.ContentResolver;
import android.database.Cursor;

import dnd.combattracker.models.Creature;
import dnd.combattracker.repository.CombatTrackerContract;
import dnd.combattracker.repository.CreatureProvider;

import static dnd.combattracker.repository.CombatTrackerContract.*;

public class CreatureController {
    private ContentResolver contentResolver;

    public CreatureController(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public Cursor search(String searchTerm) {
        String[] projection = {CreatureEntry._ID, CreatureEntry.NAME};
        String selection = CreatureEntry.NAME + " LIKE ?";
        String selectionArgs[] = {"%" + searchTerm + "%"};

        return contentResolver.query(CreatureProvider.CONTENT_URI, projection, selection, selectionArgs, CreatureEntry.NAME + " ASC");
    }
}
