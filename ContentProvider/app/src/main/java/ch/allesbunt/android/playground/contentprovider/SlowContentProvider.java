package ch.allesbunt.android.playground.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ch.allesbunt.android.playground.contentprovider.contentprovider.BuildConfig;

/**
 * A slow content provider
 */
public class SlowContentProvider extends ContentProvider {

    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.slowcontentprovider";

    private static final String URL = "content://" + AUTHORITY;

    private static final int ITEM_BY_ID = 4;

    private static final UriMatcher uriMatcher;

    // Static uri matcher initialization.

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, "/query/*", ITEM_BY_ID);
    }

    public static String createQueryUrl(@Nullable final String query) {
        return URL
                + "/query"
                + "/" + Uri.encode(query);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        switch (uriMatcher.match(uri)) {

            case ITEM_BY_ID:

                // Matrix cursor with lot of rows.

                final MatrixCursor cursor = new MatrixCursor(new String[]{"ID"});

                for (int index = 0; index < 200_000; ++index) {
                    cursor.addRow(new Object[]{index});
                }

                return cursor;
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {

            /*
             * Get a particular item id.
             */
            case ITEM_BY_ID:
                return "vnd.android.cursor.item/vnd.allesbunt.playground.slowitem";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new UnsupportedOperationException("Insert is not supported in the slow content provider");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Delete is not supported in the slow content provider");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Update is not supported in the slow content provider");
    }
}
