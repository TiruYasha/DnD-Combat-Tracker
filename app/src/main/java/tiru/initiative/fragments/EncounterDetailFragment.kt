package tiru.initiative.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_manage_encounter_detail.*
import tiru.initiative.R
import tiru.initiative.models.Encounter
import tiru.initiative.presenters.BasePresenter
import tiru.initiative.presenters.EncounterDetailsPresenter
import tiru.initiative.presenters.EncounterDetailsView
import java.util.concurrent.Future

class EncounterDetailFragment : Fragment(), EncounterDetailsView {

    private lateinit var presenter: BasePresenter
    private lateinit var listener: EncounterDetailFragmentListener

    companion object {
        fun newInstance(): EncounterDetailFragment{
            return EncounterDetailFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_manage_encounter_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listener.onEncounterDetailsFragmentCreated(this)

        input_encounter_name.addTextChangedListener(NameChanged())
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is EncounterDetailFragmentListener){
            listener = context
        }else{
            throw RuntimeException("The activity ${context.toString()} does not implement EncounterDetailFragmentListener")
        }
    }

    override fun setPresenter(presenter: EncounterDetailsPresenter) {
        this.presenter = presenter
    }

    override fun refreshDetails(encounter: Encounter) {
        input_encounter_name.setText(encounter.name)
    }

    inner class NameChanged : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            listener.onEncounterNameChanged(input_encounter_name.text.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }
}

interface EncounterDetailFragmentListener{
    fun onEncounterDetailsFragmentCreated(fragment: EncounterDetailFragment)
    fun onEncounterNameChanged(name: String): Future<Unit>
}