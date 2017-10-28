package tiru.initiative.presenters

import android.app.LoaderManager
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import tiru.initiative.activities.LoaderProvider
import tiru.initiative.activities.ManageEncounterActivity
import tiru.initiative.models.Encounter


class EncounterDetailsPresenter(private val view: EncounterDetailsView,
                                private val loaderManager: LoaderManager,
                                private val loaderProvider: LoaderProvider,
                                private var encounterDraftId: Long) : BasePresenter, LoaderManager.LoaderCallbacks<Cursor> {

    init{
        view.setPresenter(this)
    }

    override fun start() {

    }

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<Cursor> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoaderReset(p0: Loader<Cursor>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadFinished(p0: Loader<Cursor>?, p1: Cursor?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun loadDetails(encounter: Encounter) {
        view.refreshDetails(encounter)
        encounterDraftId = encounter.id
    }
}

interface EncounterDetailsView : BaseView<EncounterDetailsPresenter> {
    fun refreshDetails(encounter: Encounter)
}