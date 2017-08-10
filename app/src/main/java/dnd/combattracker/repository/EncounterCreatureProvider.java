package dnd.combattracker.repository;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static dnd.combattracker.repository.CombatTrackerContract.*;

public class EncounterCreatureProvider extends ContentProvider {

    private CombatTrackerDbHelper dbHelper;

    private static final int ENCOUNTER_CREATURE = 10;
    private static final int ENCOUNTER_CREATURE_DRAFT = 11;

    private static final String AUTHORITY = "dnd.combattracker.repository.EncounterCreatureProvider";

    private static final String BASE_PATH = "encounterCreatures";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final Uri CONTENT_URI_DRAFT = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH + "/draft");

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH, ENCOUNTER_CREATURE);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH + "/draft", ENCOUNTER_CREATURE_DRAFT);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new CombatTrackerDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (URI_MATCHER.match(uri)) {
            case ENCOUNTER_CREATURE:
                queryBuilder.setTables(EncounterCreatureEntry.TABLE_NAME);
                break;
            case ENCOUNTER_CREATURE_DRAFT:
                String CreatureTable = CreatureEntry.TABLE_NAME;
                String EncounterCreatureTable = EncounterCreatureDraftEntry.TABLE_NAME;

                String join = CreatureTable + " INNER JOIN " +
                        EncounterCreatureTable +
                        " ON " + CreatureTable + "." + CreatureEntry._ID +
                        " = " + EncounterCreatureTable + "." + EncounterCreatureDraftEntry.CREATURE_ID;
                queryBuilder.setTables(join);
                break;
            default:
                throw new IllegalArgumentException("Unknown UrI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder, null);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;

        switch (uriType) {
            case ENCOUNTER_CREATURE:
                id = db.insert(EncounterCreatureEntry.TABLE_NAME, null, values);
                break;
            case ENCOUNTER_CREATURE_DRAFT:
                id = db.insert(EncounterCreatureDraftEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unkown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int numberOfRows;

        switch (uriType) {
            case ENCOUNTER_CREATURE:
                numberOfRows = db.delete(EncounterCreatureEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ENCOUNTER_CREATURE_DRAFT:
                numberOfRows = db.delete(EncounterCreatureDraftEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unkown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return numberOfRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int amountInserted = 0;

        switch (uriType) {
            case ENCOUNTER_CREATURE:
                for (ContentValues data : values) {
                    db.insert(EncounterCreatureEntry.TABLE_NAME, null, data);
                    amountInserted++;
                }
                break;
            case ENCOUNTER_CREATURE_DRAFT:
                for (ContentValues data : values) {
                    db.insert(EncounterCreatureDraftEntry.TABLE_NAME, null, data);
                    amountInserted++;
                }
                break;
            default:
                throw new IllegalArgumentException("Unkown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return amountInserted;
    }
}
