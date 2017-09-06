package cisgvsu.biotowerdefense;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Kelsey on 9/6/2017.
 */

public class Game {
    /** List of towers. */
    private List<AntibioticTower> towers = new ArrayList();

    /** Mapping of the bacteria that are in each tower's range. */
    Map<AntibioticTower, Queue<Bacteria>> bacteriaToTower = new HashMap<AntibioticTower, Queue<Bacteria>>();

    /** Create a new game object. */
    private Game() {
        // Start game with one tower
        towers.add(new AntibioticTower());

    }
}
