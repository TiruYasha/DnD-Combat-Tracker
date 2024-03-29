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


public class EncounterProvider extends ContentProvider {

    private CombatTrackerDbHelper dbHelper;

    private static final int ENCOUNTER = 10;
    private static final int ENCOUNTER_DRAFT = 11;

    private static final String AUTHORITY = "dnd.combattracker.repository.EncounterProvider";

    private static final String BASE_PATH = "encounters";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final Uri CONTENT_URI_DRAFT = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH  + "/draft");

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH, ENCOUNTER);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH + "/draft", ENCOUNTER_DRAFT);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new CombatTrackerDbHelper(
                getContext()
        );

        return true;
    }

    @Nullable
    @Override
    synchronized public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (URI_MATCHER.match(uri)) {
            case ENCOUNTER:
                queryBuilder.setTables(EncounterEntry.TABLE_NAME);
                break;
            case ENCOUNTER_DRAFT:
                queryBuilder.setTables(EncounterDraftEntry.TABLE_NAME);
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
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;

        switch (uriType) {
            case ENCOUNTER:
                id = db.insert(EncounterEntry.TABLE_NAME, null, contentValues);
                break;
            case ENCOUNTER_DRAFT:
                id = db.insert(EncounterDraftEntry.TABLE_NAME, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unkown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] args) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int numberOfRows;

        switch (uriType) {
            case ENCOUNTER:
                numberOfRows = db.delete(EncounterEntry.TABLE_NAME, where, args);
                break;
            case ENCOUNTER_DRAFT:
                numberOfRows = db.delete(EncounterDraftEntry.TABLE_NAME, where, args);
                break;
            default:
                throw new IllegalArgumentException("Unkown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return numberOfRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int success;

        switch (uriType) {
            case ENCOUNTER:
                success = db.update(EncounterEntry.TABLE_NAME, contentValues, where, whereArgs);
                break;
            case ENCOUNTER_DRAFT:
                success = db.update(EncounterDraftEntry.TABLE_NAME, contentValues, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unkown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return success;
    }
}
