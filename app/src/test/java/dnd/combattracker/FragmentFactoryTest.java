package dnd.combattracker;

import android.support.v4.app.Fragment;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class FragmentFactoryTest {

    FragmentFactory fragmentFactory;

    public FragmentFactoryTest(){
        fragmentFactory = new FragmentFactory();
    }

    @Test
    public void getAddEncounterFragment(){
        Fragment fragment = fragmentFactory.getFragment("AddEncounterFragment");

        assertThat(fragment, instanceOf(AddEncounterFragment.class));
    }
}
