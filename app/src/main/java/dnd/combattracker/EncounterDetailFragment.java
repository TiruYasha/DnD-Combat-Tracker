package dnd.combattracker;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dnd.combattracker.repository.EncounterProvider;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EncounterDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EncounterDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EncounterDetailFragment extends Fragment {

    private long encounterDraftId;
    private OnFragmentInteractionListener mListener;
    private TextInputEditText encounterName;

    public EncounterDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EncounterDetailFragment.
     * @param encounterDraftId
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
        Bundle b = getArguments();
        if(b != null){
            encounterDraftId = b.getLong("encounterDraftId");
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encounter_detail, container, false);

        encounterName = (TextInputEditText) view.findViewById(R.id.input_encounter_name);
        encounterName.addTextChangedListener(new HandleTextChange());
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void createNewDraftIfNotExists() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class HandleTextChange implements TextWatcher{
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

            String selectionClause = "_id = ?";
            String[] selectionArgs = {String.valueOf(encounterDraftId)};
            getActivity().getContentResolver().update(EncounterProvider.CONTENT_URI_DRAFT, values, selectionClause, selectionArgs);
        }
    }
}
