package ch.allesbunt.android.playground.contentprovider;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;

import ch.allesbunt.android.playground.contentprovider.contentprovider.R;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getName();

    private static final int LOADER_ID = 0;
    private static final String LOADER_SEARCH_QUERY = TAG + ":loaderSearchQuery";

    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set strict mode.

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        // Initialize loader.

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        // Setup search view.

        final SearchView searchView = (SearchView) findViewById(R.id.searchview);
        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                final Bundle bundle = new Bundle();
                bundle.putString(LOADER_SEARCH_QUERY, newText);

                MainActivity.this.getSupportLoaderManager().restartLoader(LOADER_ID, bundle, MainActivity.this);
                return false;
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        if (id == LOADER_ID) {

            Log.d(TAG, "Loader Create");

            String searchQuery = "";

            if (bundle != null && bundle.containsKey(LOADER_SEARCH_QUERY)) {
                searchQuery = bundle.getString(LOADER_SEARCH_QUERY);
            }

            final String url = SlowContentProvider.createQueryUrl(searchQuery);

            return new CursorLoader(
                    this,
                    Uri.parse(url),
                    null,
                    null,
                    null,
                    null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "Loader Finished");

        final Cursor oldCursor = this.cursor;
        this.cursor = cursor;

        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "Loader Reset");

        if (cursor != null) {
            cursor.close();
        }
    }
}
