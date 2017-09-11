package dnd.combattracker.activities.manageencounter;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import dnd.combattracker.R;
import dnd.combattracker.adapters.ManageEncounterPagerAdapter;
import dnd.combattracker.controllers.CreatureController;
import dnd.combattracker.controllers.EncounterController;
import dnd.combattracker.controllers.EncounterCreatureController;
import dnd.combattracker.controllers.EncounterCreatureDraftController;
import dnd.combattracker.controllers.EncounterDraftController;

public class ManageEncounterActivity extends AppCompatActivity implements CreatureSearchFragment.CreatureSearchFragmentListener, EncounterDetailFragment.EncounterDetailFragmentListener {

    private ManageEncounterPagerAdapter manageEncounterPagerAdapter;

    private EncounterController encounterController;
    private EncounterDraftController encounterDraftController;
    private EncounterCreatureController encounterCreatureController;
    private EncounterCreatureDraftController encounterCreatureDraftController;
    private CreatureController creatureController;

    private long encounterId = -1;
    private long encounterDraftId = -1;

    private ViewPager viewPager;
    private EditText searchCreature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_encounter);

        encounterController = new EncounterController(getContentResolver());
        encounterDraftController = new EncounterDraftController(getContentResolver());
        encounterCreatureController = new EncounterCreatureController(getContentResolver());
        encounterCreatureDraftController = new EncounterCreatureDraftController(getContentResolver());
        creatureController = new CreatureController(getContentResolver());

        searchCreature = (EditText) findViewById(R.id.searchCreatureText);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        if (b != null && b.getLong("encounterId") != -1) {
            encounterId = b.getLong("encounterId");
        }

        setupDraft();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupDraft() {
        if (encounterExists()) {
            setupExistingEncounterDraft();
        } else {
            setupNewEncounterDraft();
        }
    }

    private void setupExistingEncounterDraft() {
        int draftCount = encounterDraftController.getDraftCountById(encounterId);

        switch (draftCount) {
            case 0:
                encounterDraftId = encounterDraftController.insertDraftFromEncounter(encounterId);
                break;
            case 1:
                encounterDraftId = encounterDraftController.insertDraftFromEncounter(encounterId);
                showRestoreDraftSnackbar();
                break;
            default:
                encounterDraftId = encounterDraftController.insertDraftFromEncounter(encounterId);
                encounterDraftController.deleteOldestDraftForEncounterId(encounterId);
                showRestoreDraftSnackbar();
                break;
        }

        setupPager();
    }

    private void setupNewEncounterDraft() {
        int draftCount = encounterDraftController.getDraftCount();

        switch (draftCount) {
            case 0:
                encounterDraftId = encounterDraftController.insertEmptyEncounterDraft();
                break;
            case 1:
                encounterDraftId = encounterDraftController.insertEmptyEncounterDraft();
                showRestoreDraftSnackbar();
                break;
            default:
                encounterDraftId = encounterDraftController.insertEmptyEncounterDraft();
                encounterDraftController.deleteOldestDraft();
                showRestoreDraftSnackbar();
                break;
        }

        setupPager();
    }

    private void showRestoreDraftSnackbar() {
        Snackbar.make(findViewById(R.id.manage_layout), "Restore previous data", Snackbar.LENGTH_LONG)
                .setAction("Restore", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        encounterDraftController.deleteDraftById(encounterDraftId);
                        if (encounterExists()) {
                            encounterDraftId = encounterDraftController.getOldestDraftIdByEncounterId(encounterId);
                        } else {
                            encounterDraftId = encounterDraftController.getOldestDraftId();
                        }
                        manageEncounterPagerAdapter.changeEncounterDraftId(encounterDraftId);
                    }
                }).show();
    }

    private void setupPager() {
        manageEncounterPagerAdapter = new ManageEncounterPagerAdapter(getSupportFragmentManager(), encounterDraftId);

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(manageEncounterPagerAdapter);
        viewPager.addOnPageChangeListener(new PageChangedListener());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manage_encounter, menu);

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
            Cursor encounterDraft = encounterDraftController.getEncounterDraftFromId(encounterDraftId);

            encounterController.updateEncounterFromDraft(encounterId, encounterDraft);
            encounterCreatureController.insertCreaturesFromDraftId(encounterId, encounterDraftId);

            encounterDraftController.deleteDraftByEncounterId(encounterId);
        } else {
            Cursor encounterDraft = encounterDraftController.getEncounterDraftFromId(encounterDraftId);

            long newEncounterId = encounterController.insertEncounterFromDraft(encounterDraft);
            encounterCreatureController.insertCreaturesFromDraftId(newEncounterId, encounterDraftId);
            encounterDraftController.deleteDraftWithoutEncounterId();
        }

        finish();
    }

    @Override
    public void onAddCreature(long creatureId, String creatureName) {
        encounterCreatureDraftController.insertCreatureForEncounter(encounterDraftId, creatureId);

        Snackbar.make(findViewById(R.id.manage_layout), "Added " + creatureName + " successfully", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    @Override
    public Cursor searchCreature(String searchTerm) {
        return creatureController.search(searchTerm);
    }

    @Override
    public void onEncounterDetailsChanged(ContentValues values) {
        encounterDraftController.updateEncounterDraftById(encounterDraftId, values);
    }

    private boolean encounterExists() {
        return encounterId != -1;
    }

    private class PageChangedListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 1) {
                searchCreature.setVisibility(View.VISIBLE);
            } else {
                searchCreature.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
