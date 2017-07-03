package dnd.combattracker;

import android.support.v4.app.Fragment;

public class FragmentFactory {
    public Fragment getFragment(String fragmentName){
        Fragment fragment = null;

        switch (fragmentName){
            case "AddEncounterFragment":
                //fragment = new AddEncounterFragment();
                break;
        }

        return fragment;
    }
}
