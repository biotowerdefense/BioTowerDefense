package cisgvsu.biotowerdefense;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Vector;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Kelsey on 9/18/2017.
 */

public class GameTest {


    @Test
    public void shootBacteria() throws Exception {
        Game g = new Game();
        Vector<AntibioticTower> towers = g.addTower(AntibioticType.penicillin, 0);
        g.addBacteria(BacteriaType.pneumonia);
        assertEquals(g.shootBacteria(towers.get(0)), true);
    }
}
