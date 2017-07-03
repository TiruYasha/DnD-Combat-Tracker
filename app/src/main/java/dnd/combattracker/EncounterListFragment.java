package dnd.combattracker;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dnd.combattracker.adapters.EncounterCursorRecyclerViewAdapter;
import dnd.combattracker.repository.EncounterProvider;

import static dnd.combattracker.repository.EncounterContract.EncounterEntry;


public class EncounterListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public final int offset = 30;
    private int page = 0;
    private RecyclerView recyclerView;
    private boolean loadingMore = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        EncounterCursorRecyclerViewAdapter adapter = new EncounterCursorRecyclerViewAdapter(getContext(), null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new ScrollListener());

        getActivity().getSupportLoaderManager().restartLoader(0, null, this);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 0:
                return new CursorLoader(getContext(), EncounterProvider.urlForItems(offset * page), null, null, null, null);
            default:
                throw new IllegalArgumentException("No id handled!");
        }
    }

    private Handler handlerToWait = new Handler();

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case 0:
                Cursor cursor = ((EncounterCursorRecyclerViewAdapter) recyclerView.getAdapter()).getCursor();

                MatrixCursor mx = new MatrixCursor(EncounterEntry.Columns);
                fillMx(cursor, mx);

                fillMx(data, mx);

                ((EncounterCursorRecyclerViewAdapter) recyclerView.getAdapter()).swapCursor(mx);

                handlerToWait.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingMore = false;
                    }
                }, 2000);
                break;
            default:
                throw new IllegalArgumentException("No loader id handled!");
        }
    }

    private void fillMx(Cursor data, MatrixCursor mx) {
        if (data == null) {
            return;
        }

        data.moveToPosition(-1);
        while (data.moveToNext()) {
            mx.addRow(new Object[]{
                    data.getString(data.getColumnIndex(EncounterEntry._ID)),
                    data.getString(data.getColumnIndex(EncounterEntry.COLUMN_NAME))
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class ScrollListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            refreshCursor(recyclerView);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            refreshCursor(recyclerView);
        }

        private void refreshCursor(RecyclerView recyclerView) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            int maxPositions = layoutManager.getItemCount();

            if (lastVisibleItemPosition == maxPositions - 1) {
                if (loadingMore)
                    return;

                loadingMore = true;
                page++;
                getActivity().getSupportLoaderManager().restartLoader(0, null, EncounterListFragment.this);
            }
        }
    }
}
