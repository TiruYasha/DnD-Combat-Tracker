package dnd.combattracker.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import dnd.combattracker.activities.manageencounter.CreatureSearchFragment;
import dnd.combattracker.activities.manageencounter.EncounterDetailFragment;

public class ManageEncounterPagerAdapter extends FragmentStatePagerAdapter {

    private long encounterDraftId = -1;

    public ManageEncounterPagerAdapter(FragmentManager fm, long encounterDraftId) {
        super(fm);
        this.encounterDraftId = encounterDraftId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return EncounterDetailFragment.newInstance(encounterDraftId);
            case 1:
                return CreatureSearchFragment.newInstance(encounterDraftId);
            default:
                return EncounterDetailFragment.newInstance(encounterDraftId);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Encounter";
            case 1:
                return "Creatures";
            case 2:
                return "Players";
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void changeEncounterDraftId(long encounterDraftId){
        this.encounterDraftId = encounterDraftId;
        notifyDataSetChanged();
    }
}
