package dnd.combattracker.activities.manageencounter;

import android.content.Context;
import android.database.Cursor;
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
import android.widget.TextView;

import dnd.combattracker.R;
import dnd.combattracker.adapters.CreatureSearchAdapter;
import dnd.combattracker.listeners.OnItemClickListener;
import dnd.combattracker.repository.CombatTrackerContract;
import dnd.combattracker.repository.CreatureProvider;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CreatureSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatureSearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListener {

    private RecyclerView recyclerView;
    private CreatureSearchAdapter adapter;
    private LinearLayoutManager layoutManager;
    private CreatureSearchFragmentListener listener;


    public CreatureSearchFragment() {
        // Required empty public constructor
    }

    public static CreatureSearchFragment newInstance(long encounterDraftId) {
        CreatureSearchFragment fragment = new CreatureSearchFragment();
        Bundle args = new Bundle();
        args.putLong("encounterDraftId", encounterDraftId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CreatureSearchAdapter(null);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {CombatTrackerContract.CreatureEntry._ID, CombatTrackerContract.CreatureEntry.NAME};
        return new CursorLoader(getActivity(), CreatureProvider.CONTENT_URI, projection, null, null, CombatTrackerContract.CreatureEntry.NAME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(View itemView, int position) {
        switch (itemView.getId()) {
            case R.id.button_add_creature:
                addCreatureToEncounter(position);
                break;
        }
    }

    private void addCreatureToEncounter(int position) {
        adapter.getCursor().moveToPosition(position);

        String creatureName = adapter.getCursor().getString(adapter.getCursor().getColumnIndex(CombatTrackerContract.CreatureEntry.NAME));
        listener.onAddEncounter(adapter.getItemId(position), creatureName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreatureSearchFragmentListener) {
            listener = (CreatureSearchFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CreatureSearchFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface CreatureSearchFragmentListener {
        void onAddEncounter(long creatureId, String creatureName);
    }
}
