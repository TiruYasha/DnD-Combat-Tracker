package dnd.combattracker;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AddEncounterFragment extends Fragment implements View.OnClickListener{
    Button confirm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_encounter_fragment, container, false);

        confirm = (Button) v.findViewById(R.id.confirmAddEncounter);
        confirm.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.confirmAddEncounter:
                Snackbar.make(getView(), "Add encounter.....", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }
    }
}
