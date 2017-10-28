package tiru.initiative.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_manage_encounter.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import tiru.initiative.R
import tiru.initiative.adapters.ManageEncounterPagerAdapter
import tiru.initiative.fragments.EncounterDetailFragment
import tiru.initiative.fragments.EncounterDetailFragmentListener
import tiru.initiative.presenters.EncounterDetailsPresenter
import tiru.initiative.presenters.EncounterDetailsView
import tiru.initiative.services.EncounterService
import java.util.concurrent.Future

class ManageEncounterActivity : AppCompatActivity(), EncounterDetailFragmentListener {
    private lateinit var encounterDetailsPresenter: EncounterDetailsPresenter

    private lateinit var pagerAdapter: ManageEncounterPagerAdapter
    private lateinit var service: EncounterService
    private var encounterId: Long? = null
    private var encounterDraftId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        service = EncounterService(contentResolver)

        setContentView(R.layout.activity_manage_encounter)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        setupDraft()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_manage_encounter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_add_encounter -> {
                service.saveEncounterFromDraft(encounterId, encounterDraftId)
                finish()
                return true
            }
            R.id.action_restore_draft -> {
                restoreDraft()
                return true
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onEncounterDetailsFragmentCreated(fragment: EncounterDetailFragment) {
        encounterDetailsPresenter = EncounterDetailsPresenter(fragment, loaderManager, LoaderProvider(this), encounterDraftId)
    }

    private fun initializePageradapter() {
        pagerAdapter = ManageEncounterPagerAdapter(supportFragmentManager)

        container.adapter = pagerAdapter

        tabs.setupWithViewPager(container)
    }

    private fun showRestoreDraftMessage() {
        Snackbar.make(main_content, "Restore previous data", Snackbar.LENGTH_LONG)
                .setAction("Restore", {
                    restoreDraft()
                }).show()
    }

    override fun onEncounterNameChanged(name: String): Future<Unit> = doAsync {
        service.updateDraftName(encounterDraftId, name)
    }

    private fun setupDraft() = doAsync {
        encounterDraftId = service.setupDraft(encounterId)

        uiThread {
            initializePageradapter()
        }

        if(service.getDraftCountByEncounterId(encounterId) > 1){
            uiThread {
                showRestoreDraftMessage()
            }
        }
    }

    private fun restoreDraft() = doAsync{
        val encounter = service.restoreDraft(encounterId)
        encounterDraftId = encounter.id
        uiThread {
            encounterDetailsPresenter.loadDetails(encounter)
        }
    }
}
