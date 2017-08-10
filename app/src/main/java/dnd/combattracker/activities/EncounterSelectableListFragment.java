package dnd.combattracker.activities;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dnd.combattracker.R;
import dnd.combattracker.adapters.EncounterSelectableAdapter;
import dnd.combattracker.controllers.EncounterController;
import dnd.combattracker.listeners.OnItemClickListener;
import dnd.combattracker.repository.CombatTrackerContract;
import dnd.combattracker.repository.EncounterProvider;

public class EncounterSelectableListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListener{

    private EncounterController encounterController;
    private RecyclerView recyclerView;
    private EncounterSelectableAdapter adapter;
    private LinearLayoutManager layoutManager;

    private int initialSelected = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        view.setTag("RecyclerViewFragment");

        setHasOptionsMenu(true);

        encounterController = new EncounterController(getActivity().getContentResolver());

        initializeRecyclerView(view);

        return view;
    }

    private void initializeRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EncounterSelectableAdapter(null);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        initialSelected = getArguments().getInt("selected");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.encounter_selectable_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_select_all:
                adapter.selectAll();
                break;
            case R.id.menu_deselect_all:
                adapter.clearSelection();
                break;
            case R.id.menu_delete_selected:
                deleteEncounters();
                break;
        }
        refreshTitle();

        return super.onOptionsItemSelected(item);
    }

    private void deleteEncounters() {
        if(adapter.getSelectedItems().size() < 1){
            Toast.makeText(getActivity(), "Nothing selected", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Integer> positions = adapter.getSelectedItems();
        List<String> encounterIds = new ArrayList<>();

        getEncountersToBeDeleted(positions, encounterIds);

        encounterController.deleteEncounters(encounterIds);

        adapter.clearSelection();
        refreshTitle();
    }

    private void getEncountersToBeDeleted(List<Integer> positions, List<String> encounterIds) {
        Cursor cursor = adapter.getCursor();
        cursor.moveToPosition(-1);

        while(cursor.moveToNext()){
            if(positions.contains(cursor.getPosition())){
                encounterIds.add("" + cursor.getInt(cursor.getColumnIndex(CombatTrackerContract.EncounterEntry._ID)));
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EncounterProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);

        if(initialSelected > -1){
            adapter.toggleSelection(initialSelected);
            initialSelected = -1;
            refreshTitle();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(View itemView, int position) {
        adapter.toggleSelection(position);
        refreshTitle();
    }

    private void refreshTitle() {
        int amountSelected = adapter.getSelectedItems().size();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(amountSelected + " Selected");
    }
}
