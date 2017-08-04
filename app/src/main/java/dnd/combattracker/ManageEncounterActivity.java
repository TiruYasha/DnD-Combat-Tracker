package dnd.combattracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import dnd.combattracker.repository.EncounterProvider;

import static dnd.combattracker.repository.CombatTrackerContract.EncounterEntry;


public class ManageEncounterActivity extends AppCompatActivity implements EncounterDetailFragment.OnFragmentInteractionListener {

    private long encounterId = -1;
    private long encounterDraftId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_encounter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null && savedInstanceState.getInt("encounterId") != -1) {
            encounterId = savedInstanceState.getInt("encounterId");
        }

        createDraft(savedInstanceState);

        loadEncounterDetailFragment();
    }

    private void createDraft(Bundle savedInstanceState) {
        if (encounterId == -1) {
            ContentValues values = new ContentValues();
            values.put(EncounterEntry.NAME, "");

            Uri result = getContentResolver().insert(EncounterProvider.CONTENT_URI_DRAFT, values);
            encounterDraftId = Long.parseLong(result.getLastPathSegment());
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

        if(encounterId != -1){
            updateEncounterToDraft();
        }else{
            insertEncounterFromDraft();
        }
//        ContentValues values = new ContentValues();
//        values.put(EncounterEntry.NAME, encounterName.getText().toString());
//
//        getContentResolver().insert(EncounterProvider.CONTENT_URI, values);

        finish();
    }

    private void insertEncounterFromDraft() {
        String[] projection = {"name"};
        String selection = "_id = ?";
        String[] selectionArgs = {String.valueOf(encounterDraftId)};

        Cursor cursor = getContentResolver().query(EncounterProvider.CONTENT_URI_DRAFT, projection, selection, selectionArgs, null);

        ContentValues data = new ContentValues();

        while(cursor.moveToNext()){
            data.put("name", cursor.getString(cursor.getColumnIndex("name")));
        }

        getContentResolver().insert(EncounterProvider.CONTENT_URI, data);
    }

    private void updateEncounterToDraft() {


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
}
