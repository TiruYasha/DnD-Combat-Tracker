package dnd.combattracker.repository;


import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class CombatTrackerDbHelper extends SQLiteAssetHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CombatTracker.db";

    public CombatTrackerDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
