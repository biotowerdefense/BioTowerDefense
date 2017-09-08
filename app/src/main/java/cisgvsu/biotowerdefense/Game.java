package cisgvsu.biotowerdefense;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Kelsey on 9/6/2017.
 */

public class Game {
    /** List of towers, thread-safe. */
    private List<AntibioticTower> towers = Collections.synchronizedList(new ArrayList());

    /** Mapping of the bacteria that are in each tower's range. */
    Map<AntibioticTower, Queue<Bacteria>> bacteriaToTower = new HashMap<AntibioticTower, Queue<Bacteria>>();

    /** Antibiotic resistance for new bacteria. */
    Map<BacteriaType, List<AntibioticType>> resistances = new HashMap<BacteriaType, List<AntibioticType>>();

    /** Create a new game object. */
    private Game() {
        // Start game with one tower - penicillin at first location
        towers.add(new AntibioticTower(AntibioticType.penicillin, 0));

    }

    /**
     * Inspect the first bacteria, determine if one shot from tower
     * will kill it. If so, remove it from queue. Otherwise, decrement
     * its health and leave it there.
     * @param tower
     */
    private void shootBacteria(AntibioticTower tower) {
        Queue<Bacteria> bacteria = bacteriaToTower.get(tower);
        Bacteria first = bacteria.peek();
        int health = first.getHealth();
        int power = tower.getPower();

        if (first != null && !resistant(first, tower.getType())) {
            if (power >= health) {
                bacteria.remove();
            } else {
                first.setHealth(health - power);
            }
        }
    }

    /**
     * First check if bacteria is already resistant to the antibiotic,
     * then run resistance algorithm to determine if this hit will
     * make it resistant. Return true if resistant, false otherwise.
     * @param bacteria
     * @return
     */
    private boolean resistant(Bacteria bacteria, AntibioticType antibiotic) {
        if (bacteria.isResistantTo(antibiotic)) {
            return true;
        } else {
            // TODO: Actually implement this
            return false;
        }
    }

    private void addBacteria(BacteriaType type) {
        // Create the new bacteria
        Bacteria bacteria = new Bacteria(type, 1);

        // Add it to the first tower's queue
        AntibioticTower firstTower = towers.get(0);
        bacteriaToTower.get(firstTower).add(bacteria);
    }

}
