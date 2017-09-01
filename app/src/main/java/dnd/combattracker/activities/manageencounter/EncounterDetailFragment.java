package dnd.combattracker.activities.manageencounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dnd.combattracker.R;
import dnd.combattracker.adapters.EncounterCreatureAdapter;
import dnd.combattracker.controllers.EncounterController;
import dnd.combattracker.models.Creature;
import dnd.combattracker.repository.EncounterCreatureProvider;

import static dnd.combattracker.repository.CombatTrackerContract.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link EncounterDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EncounterDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Cursor cursor;
    private EncounterController encounterController;
    private long encounterDraftId;

    private TextInputEditText encounterName;

    private RecyclerView recyclerView;
    private EncounterCreatureAdapter adapter;
    private LinearLayoutManager layoutManager;

    public EncounterDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param encounterDraftId
     * @return A new instance of fragment EncounterDetailFragment.
     */
    public static EncounterDetailFragment newInstance(long encounterDraftId) {
        EncounterDetailFragment fragment = new EncounterDetailFragment();

        Bundle args = new Bundle();
        args.putLong("encounterDraftId", encounterDraftId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        encounterController = new EncounterController(getActivity().getContentResolver());

        Bundle b = getArguments();
        if (b != null) {
            encounterDraftId = b.getLong("encounterDraftId");
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encounter_detail, container, false);

        cursor = encounterController.getEncounterDraftFromId(encounterDraftId);

        encounterName = (TextInputEditText) view.findViewById(R.id.input_encounter_name);
        encounterName.addTextChangedListener(new HandleTextChange());

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EncounterCreatureAdapter(null);
        recyclerView.setAdapter(adapter);
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        while (cursor.moveToNext()) {
            encounterName.setText(cursor.getString(cursor.getColumnIndex(EncounterDraftEntry.NAME)));
        }

        cursor.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {CreatureEntry.TABLE_NAME + "." + CreatureEntry._ID, CreatureEntry.NAME};
        String selection = EncounterCreatureDraftEntry.ENCOUNTER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(encounterDraftId)};

        return new CursorLoader(getActivity(), EncounterCreatureProvider.CONTENT_URI_DRAFT, projection, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        String test = "0";
    }

    private class HandleTextChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            ContentValues values = new ContentValues();

            values.put("name", encounterName.getText().toString());

            encounterController.updateEncounterDraftById(encounterDraftId, values);
        }
    }
}
