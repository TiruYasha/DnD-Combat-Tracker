package dnd.combattracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import dnd.combattracker.adapters.EncounterAdapter;
import dnd.combattracker.repository.EncounterProvider;

import static dnd.combattracker.repository.EncounterContract.EncounterEntry;
import static dnd.combattracker.repository.EncounterProvider.CONTENT_URI;
import static dnd.combattracker.repository.EncounterProvider.urlForItems;

public class EncounterListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        view.setTag("RecyclerViewFragment");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EncounterAdapter(new String[]{"test", "test2", "test3", "test4", "test 5"}, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onClick(View view) {
        addEncounter();
    }


    private void addEncounter() {
        ContentValues values = new ContentValues();
        values.put(EncounterEntry.COLUMN_NAME, "testestest");

//        db.insert(EncounterEntry.TABLE_NAME, null, values);
        Uri encounterUri = getActivity().getContentResolver().insert(CONTENT_URI, values);
        //onBackPressed();
        //finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EncounterProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int test = 1;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
