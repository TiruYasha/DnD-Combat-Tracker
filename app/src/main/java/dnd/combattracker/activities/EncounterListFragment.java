package dnd.combattracker.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dnd.combattracker.R;
import dnd.combattracker.activities.manageencounter.ManageEncounterActivity;
import dnd.combattracker.adapters.EncounterAdapter;
import dnd.combattracker.listeners.OnItemClickListener;
import dnd.combattracker.listeners.OnLongItemClickListener;
import dnd.combattracker.repository.EncounterProvider;

public class EncounterListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListener, OnLongItemClickListener {

    private RecyclerView recyclerView;
    private EncounterAdapter adapter;
    private LinearLayoutManager layoutManager;
    private OpenSelectableListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        view.setTag("RecyclerViewFragment");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EncounterAdapter(null);
        adapter.setOnItemClickListener(this);
        adapter.setOnLongItemClickListener(this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Encounters");
    }

    //region CursorLoader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EncounterProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    //endregion


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OpenSelectableListener){
            listener = (OpenSelectableListener) context;
        } else{
            throw new RuntimeException(context.toString() + " must implement OpenSelectableListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Intent intent = new Intent(getActivity(), ManageEncounterActivity.class);
        Bundle b = new Bundle();

        b.putLong("encounterId", adapter.getItemId(position));
        intent.putExtras(b);
        startActivity(intent);
    }


    @Override
    public void onLongItemClick(View itemView, int position) {
        listener.openSelectable(position);;
//        EncounterSelectableListFragment fragment = new EncounterSelectableListFragment();
//        Bundle args = new Bundle();
//        args.putInt("selected", position);
//        fragment.setArguments(args);
//
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        fragmentTransaction.addToBackStack("selector");
//        fragmentTransaction.replace(R.id.frame_layout_main, fragment);
//
//        fragmentTransaction.commit();
    }

    public interface OpenSelectableListener{
        void openSelectable(int selectedItem);
    }
}
