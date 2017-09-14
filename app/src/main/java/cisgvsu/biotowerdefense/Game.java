package cisgvsu.biotowerdefense;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * This class controls the interaction of antibiotic towers and bacteria.
 */

public class Game {
    /** List of towers, thread-safe. */
    private List<AntibioticTower> towers = Collections.synchronizedList(new ArrayList());

    /** Mapping of the bacteria that are in each tower's range, thread-safe. */
    Map<AntibioticTower, Queue<Bacteria>> bacteriaToTower = Collections.synchronizedMap(new HashMap<AntibioticTower, Queue<Bacteria>>());

    /** Antibiotic resistance for new bacteria, thread-safe. */
    Map<BacteriaType, List<AntibioticType>> resistances = Collections.synchronizedMap(new HashMap<BacteriaType, List<AntibioticType>>());

    /** Create a new game object. */
    private Game() {
        // Start game with one tower - penicillin at first location
        towers.add(new AntibioticTower(AntibioticType.penicillin, 0));
        this.activateTower(towers.get(0));

        // Add bacteria to game
        this.startAddingBacteria();
    }

    /**
     * Inspect the first bacteria, determine if one shot from tower
     * will kill it. If so, remove it from queue. Otherwise, decrement
     * its health and leave it there.
     * @param tower The tower that is currently shooting.
     */
    private void shootBacteria(AntibioticTower tower) {
        Queue<Bacteria> bacteria = bacteriaToTower.get(tower);
        Bacteria first = bacteria.peek();

        if (first != null && !resistant(first, tower.getType())) {
            int health = first.getHealth();
            int power = tower.getPower();

            if (power >= health) {
                bacteria.remove();
            } else {
                first.setHealth(health - power);
            }
        }
    }

    /**
     * This method returns whether or not the specific bacteria passed in
     * is resistant to the antibiotic. There are several cases.
     *
     * Case 1: Bacteria type is already resistant to the antibiotic, and
     * this individual one is not exempt. Return true.
     *
     * Case 2: Bacteria type is already resistant to the antibiotic, but this
     * individual was created before they were all resistant. Run algorithm.
     *
     * Case 3: Bacteria type is not resistant at all. Run algorithm.
     *
     * @param bacteria The bacteria we're checking for resistance.
     * @param antibiotic The type of antibiotic we're checking for resistance to.
     * @return True if the bacteria is resistant, false otherwise.
     */
    private boolean resistant(Bacteria bacteria, AntibioticType antibiotic) {
        // Check if this type of bacteria is resistant to this type of antibiotic,
        // and if the specific bacteria is not exempt from resistance
        if (resistances.get(bacteria.getType()).contains(antibiotic) &&
                !bacteria.isExempt(antibiotic)) {
            return true;
        } else {
            // Run the algorithm to see if the bacteria becomes resistant.
            boolean nowResistant = this.resistanceAlgorithm(bacteria.getType(), antibiotic);

            // Update various fields to reflect the new resistance
            if (nowResistant) {
                // Update local resistance data
                if (resistances.get(bacteria) == null) {
                    List list = new ArrayList<>();
                    list.add(antibiotic);
                    resistances.put(bacteria.getType(), list);
                } else {
                    resistances.get(bacteria).add(antibiotic);
                }

                // Mark any bacteria of this type that are already created as being exempt
                // to this antibiotic
                for (AntibioticTower t : bacteriaToTower.keySet()) {
                    Bacteria b[] = (Bacteria[]) bacteriaToTower.get(t).toArray();
                    for (int i = 0; i < b.length; i++) {
                        if (b[i].getType().equals(bacteria.getType())) {
                            b[i].setExempt(antibiotic);
                        }
                    }
                }

                // TODO: Notify control class that bacteria became resistant
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     *
     * @param bacteriaType
     * @param antibiotic
     * @return
     */
    private boolean resistanceAlgorithm(BacteriaType bacteriaType, AntibioticType antibiotic) {
        // TODO: Implement me!
        return false;
    }

    /**
     * Add a new bacteria of the specified type to the end
     * of the first tower's queue.
     * @param type The type of bacteria to be added to the game.
     */
    private void addBacteria(BacteriaType type) {
        // Create the new bacteria
        Bacteria bacteria = new Bacteria(type, 1);

        // Add it to the first tower's queue
        AntibioticTower firstTower = towers.get(0);
        bacteriaToTower.get(firstTower).add(bacteria);
    }

    /**
     * Remove the bacteria at the head of the queue for the specified
     * tower, then find the next sequential tower and add the bacteria
     * to that tower's queue.
     * @param tower The tower that the bacteria is being moved away from.
     */
    private void moveBacteriaToNextTower(AntibioticTower tower) {
        Bacteria bacteria = bacteriaToTower.get(tower).remove();
        int nextIndex = towers.indexOf(tower) + 1;

        if (nextIndex > towers.size()-1) {
            System.out.println("You lose!");
            // TODO: Lose condition. There's no more towers to fight off bacteria
        } else {
            AntibioticTower next = towers.get(nextIndex);
            bacteriaToTower.get(next).add(bacteria);
        }
    }

    /**
     * Start a thread for the specified tower so that it shoots
     * at the bacteria in its range.
     * @param tower The tower that will begin shooting.
     */
    private void activateTower(AntibioticTower tower) {
        TowerThread thread = new TowerThread(tower);
        thread.start();
    }

    /**
     * Start a threat to begin adding bacteria to the game.
     */
    private void startAddingBacteria() {
        BacteriaThread t = new BacteriaThread();
        t.start();
    }

    /**
     * Thread to shoot the tower.
     */
    class TowerThread extends Thread {
        /** The tower that this thread has been started for. */
        AntibioticTower tower;

        /**
         * Constructor.
         * @param tower The tower that will be shooting.
         */
        private TowerThread(AntibioticTower tower) {
            this.tower = tower;
        }

        /**
         * Shoot at the bacteria once every second.
         */
        @Override
        public void run() {
            while (true) {
                shootBacteria(tower);
                try {
                    sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Thread to add bacteria and move them between towers as necessary.
     */
    class BacteriaThread extends Thread {
        /**
         * Add a bacteria every second. Check if the head of each tower's queue
         * is out of range, if so, move it to next tower.
         */
        @Override
        public void run() {
            while (true) {
                addBacteria(BacteriaType.staph);
                for (AntibioticTower t : towers) {
                    if (bacteriaToTower.get(t).peek().outOfRange()) {
                        moveBacteriaToNextTower(t);
                    }
                }
                try {
                    sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
