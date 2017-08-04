package dnd.combattracker;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import dnd.combattracker.controllers.EncounterController;
import dnd.combattracker.repository.CombatTrackerContract;

public class ManageEncounterActivity extends AppCompatActivity implements EncounterDetailFragment.OnFragmentInteractionListener {

    private EncounterController encounterController;
    private long encounterId = -1;
    private long encounterDraftId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        encounterController = new EncounterController(getContentResolver());

        setContentView(R.layout.activity_manage_encounter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        if (b != null && b.getLong("encounterId") != -1) {
            encounterId = b.getLong("encounterId");
        }

        createDraft();
    }

    private void createDraft() {
        if (encounterId == -1) {

            long draftId = encounterController.getDraftIdWithoutEncounterId();

            if (draftId > -1) {
                showDraftDialog(new DialogNonExistingEncounterClickListener());
                return;
            }
            return;
        }

        final long draftId = encounterController.getDraftIdByEncounterId(encounterId);

        if (draftId != -1) {
            showDraftDialog(new DialogExistingEncounterClickListener());
        } else {
            encounterDraftId = encounterController.insertDraftFromEncounter(encounterId);
            loadEncounterDetailFragment();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_encounter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add_encounter:
                saveEncounter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveEncounter() {
        if (encounterId != -1) {
            encounterController.updateEncounterFromDraft(encounterId, encounterDraftId);//updateEncounterToDraft();
        } else {
            encounterController.insertEncounterFromDraft(encounterDraftId);
        }

        finish();
    }

    private void loadEncounterDetailFragment() {

        EncounterDetailFragment fragment = EncounterDetailFragment.newInstance(encounterDraftId);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.frame_layout_main, fragment);

        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void showDraftDialog(DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Draft");
        builder.setMessage("Want to use the last draft?");
        builder.setPositiveButton("Yes", clickListener);
        builder.setNegativeButton("No", clickListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class DialogExistingEncounterClickListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which == -2){
                encounterDraftId = encounterController.insertDraftFromEncounter(encounterId);
                loadEncounterDetailFragment();
                dialog.dismiss();
            }else{
                encounterDraftId = encounterController.getDraftIdByEncounterId(encounterId);
                loadEncounterDetailFragment();
                dialog.dismiss();
            }
        }
    }

    private class DialogNonExistingEncounterClickListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            //TODO Delete old drafts
            if(which == -2){
                ContentValues values = new ContentValues();
                values.put(CombatTrackerContract.EncounterEntry.NAME, "");

                encounterDraftId = encounterController.insertEncounterDraft(values);
                loadEncounterDetailFragment();
            }else{
                encounterDraftId = encounterController.getDraftIdWithoutEncounterId();
                loadEncounterDetailFragment();
            }
        }
    }
}
