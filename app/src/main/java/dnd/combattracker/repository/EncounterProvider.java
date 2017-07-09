package dnd.combattracker.repository;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class EncounterProvider extends ContentProvider {

    private EncounterDbHelper dbHelper;

    private static final int ENCOUNTERS = 10;
    private static final int ENCOUNTER_ID = 20;

    private static final String AUTHORITY = "dnd.combattracker.repository.EncounterProvider";

    private static final String BASE_PATH = "encounters";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/encounters";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/encounters";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, BASE_PATH, ENCOUNTERS);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/offset/" + "#", ENCOUNTERS);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ENCOUNTER_ID);
    }

    public static Uri urlForItems(int limit) {
        return Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH + "/offset/" + limit);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new EncounterDbHelper(
                getContext()
        );

        return true;
    }

    @Nullable
    @Override
    synchronized public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(EncounterContract.EncounterEntry.TABLE_NAME);
        String limitArg = "0";

        switch (sUriMatcher.match(uri)) {
            case ENCOUNTERS:

                break;
            case ENCOUNTER_ID:
                queryBuilder.appendWhere(EncounterContract.EncounterEntry._ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown UrI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder, limitArg);

        cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI);


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
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;

        switch (uriType) {
            case ENCOUNTERS:
                id = db.insert(EncounterContract.EncounterEntry.TABLE_NAME, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unkown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
