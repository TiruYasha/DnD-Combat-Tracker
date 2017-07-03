package dnd.combattracker.repository;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EncounterDbHelper extends SQLiteOpenHelper {


    private static final String SQL_CREATE_ENCOUNTER = String.format("CREATE TABLE %s (" +
            "%s INTEGER PRIMARY KEY," +
            "%s TEXT)", EncounterContract.EncounterEntry.TABLE_NAME, EncounterContract.EncounterEntry._ID, EncounterContract.EncounterEntry.COLUMN_NAME);

    private static final String SQL_DELETE_ENCOUNTER = "DROP TABLE IF EXISTS " + EncounterContract.EncounterEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CombatTracker.db";

    public EncounterDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENCOUNTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENCOUNTER);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
