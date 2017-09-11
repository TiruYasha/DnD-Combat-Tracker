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

public class CreatureProvider extends ContentProvider {

    private CombatTrackerDbHelper dbHelper;

    private static final int CREATURE = 10;
    private static final int CREATURE_DRAFT = 11;

    private static final String AUTHORITY = "dnd.combattracker.repository.CreatureProvider";

    private static final String BASE_PATH = "creatures";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final Uri CONTENT_URI_DRAFT = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH + "/draft");

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH, CREATURE);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH + "/draft", CREATURE_DRAFT);
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
            case CREATURE:
                queryBuilder.setTables(CombatTrackerContract.CreatureEntry.TABLE_NAME);
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
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
