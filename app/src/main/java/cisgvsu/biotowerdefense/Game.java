package cisgvsu.biotowerdefense;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class controls the interaction of antibiotic towers and bacteria.
 * The only public methods are for starting and stopping the game, and
 * adding and removing towers.
 *
 * Kelsey Brennan
 * 9/22/17
 */
public class Game {
    /** List of towers. */
    private Vector<AntibioticTower> towers;

    /** Mapping of the bacteria that are in each tower's range.  */
    private ConcurrentHashMap<AntibioticTower, Queue<Bacteria>> bacteriaToTower;

    /** Antibiotic resistance for new bacteria. */
    private ConcurrentHashMap<BacteriaType, List<AntibioticType>> resistances;

    /** The number of towers allowed in the game. */
    private static final int NUM_TOWERS = 5;

    /** The thread that adds bacteria to the game. */
    private BacteriaThread bacteriaThread = new BacteriaThread();

    /** The set of threads that make the towers shoot. */
    private List<TowerThread> towerThreads = new ArrayList<>();

    /** Control whether te game should be running the thread to add bacteria. */
    private boolean addingBacteria;

    /**
     * Creates a new game instance by instantiating the
     * fields, making them all thread-safe.
     */
    public Game() {
        towers = new Vector<>(NUM_TOWERS);
        bacteriaToTower = new ConcurrentHashMap<>();
        resistances = new ConcurrentHashMap<>();
    }

    /**
     * Start the game by making the towers shoot
     * and bacteria move.
     */
    public void startGame() {
        addTower(AntibioticType.penicillin, 0);
        this.startAddingBacteria();
        /*for (AntibioticTower t : towers) {
            this.activateTower(t);
        }*/
    }

    /**
     * Stop the towers from shooting and bacteria from
     * moving.
     */
    public void stopGame() {
        this.addingBacteria = false;
        for (AntibioticTower t : towers) {
            t.setShooting(false);
        }
    }

    /**
     * Return all the bacteria in the game so their
     * locations can be accessed to draw them.
     *
     * @return A list of all the bacteria.
     */
    public ArrayList<Bacteria> getAllBacteria() {
        ArrayList<Bacteria> allBacteria = new ArrayList<>();
        for (Queue<Bacteria> queue : bacteriaToTower.values()) {
            for (Bacteria bacteria : queue) {
                allBacteria.add(bacteria);
            }
        }
        return allBacteria;
    }

    /**
     * Add a new tower to the game with the specified type and in the given
     * location, replacing any existing tower in that location and taking on
     * its list of bacteria.
     *
     * @param type Type of antibiotic for the tower to be added
     * @param location Position of the tower
     * @return The updated list of towers in the game, null if location is invalid.
     */
    public Vector<AntibioticTower> addTower(AntibioticType type, int location) {
        // Make sure location is valid
        if (location > NUM_TOWERS - 1 || location < 0) {
            return null;
        } else {
            // Get rid of tower currently at that location if it exists
            AntibioticTower oldTower = null;
            if (!towers.isEmpty() && towers.get(location) != null) {
                oldTower = towers.remove(location);

                // Stop shooting thread for this tower
                towerThreads.remove(location);
                oldTower.setShooting(false);
            }

            // Make a new tower and add it to the list
            AntibioticTower newTower =  new AntibioticTower(type, location);
            towers.add(location, newTower);
            towerThreads.add(location, new TowerThread(newTower));

            // Get any bacteria that may have belonged to the tower previously in this
            // location and remove it from the mapping
            LinkedList<Bacteria> bacteriaList = new LinkedList<>();
            if (oldTower != null) {
                bacteriaList = (LinkedList<Bacteria>) bacteriaToTower.remove(oldTower);
            }

            // Put the new tower in the mapping to its bacteria
            bacteriaToTower.put(newTower, bacteriaList);
            return towers;
        }
    }

    /**
     * Removes the tower at the specified location, moving any
     * bacteria up to the next tower's queue, and inserts
     * null in its place to maintain position of other towers.
     *
     * @param location The location of the tower to be removed.
     * @return Null if location is invalid, otherwise the tower
     * that was removed.
     */
    public AntibioticTower removeTower(int location) {
        if (location > NUM_TOWERS - 1 || location < 0) {
            return null;
        } else {
            // Remove from list of towers & stop thread
            AntibioticTower t = towers.remove(location);
            towerThreads.remove(location);
            t.setShooting(false);

            // Move any bacteria in its queue to next tower
            Queue<Bacteria> queue = bacteriaToTower.remove(t);
            if (queue != null) {
                Bacteria[] bacteria = (Bacteria[]) queue.toArray();
                for (int i = 0; i < bacteria.length; i++) {
                    this.moveBacteriaToNextTower(t);
                }
            }

            // Put null values in to maintain positions of other towers
            towers.add(location, null);
            towerThreads.add(location, null);
            return t;
        }
    }

    /**
     * Inspect the first bacteria, determine if one shot from tower
     * will kill it. If so, remove it from queue. Otherwise, decrement
     * its health and leave it there.
     *
     * @param tower The tower that is currently shooting.
     * @return True if the bacteria was killed, false otherwise.
     */
    private boolean shootBacteria(AntibioticTower tower) {
        Queue<Bacteria> bacteria = bacteriaToTower.get(tower);
        Bacteria first = bacteria.peek();

        if (first != null && !resistant(first, tower.getType())) {
            int health = first.getHealth();
            int power = tower.getPower();

            if (power >= health) {
                bacteria.remove();
                return true;
            } else {
                first.setHealth(health - power);
                return false;
            }
        }
        return false;
    }

    /**
     * This method returns whether or not the specific bacteria passed in
     * is resistant to the antibiotic. There are several cases.
     *
     * Case 1: Bacteria type is already resistant to the antibiotic, and
     * this individual one is too (aka not exempt). Return true.
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
        if (!resistances.isEmpty() && resistances.get(bacteria.getType()).contains(antibiotic) &&
                !bacteria.isExempt(antibiotic)) {
            return true;
        } else {
            // Run the algorithm to see if the bacteria becomes resistant.
            boolean nowResistant = this.resistanceAlgorithm(bacteria.getType(), antibiotic);

            // Update various fields to reflect the new resistance
            if (nowResistant) {
                // Update local resistance data
                if (resistances.get(bacteria.getType()) == null) {
                    List list = new ArrayList<>();
                    list.add(antibiotic);
                    resistances.put(bacteria.getType(), list);
                } else {
                    resistances.get(bacteria.getType()).add(antibiotic);
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
     * Run to determine if the bacteria type will become resistant to the
     * antibiotic type.
     *
     * @param bacteriaType The bacteria type we're checking.
     * @param antibiotic The antibiotic type we're checking.
     * @return True if resistant, false otherwise.
     */
    private boolean resistanceAlgorithm(BacteriaType bacteriaType, AntibioticType antibiotic) {
        // TODO: Implement me!
        double chance = 0;
        switch (antibiotic) {
            case penicillin:
                chance = 0.03;
                break;
            case vancomycin:
                chance = 0.01;
                break;
            case linezolid:
                chance = 0.008;
                break;
        }

        return Math.random() <= chance;
    }

    /**
     * Add a new bacteria of the specified type to the end
     * of the first tower's queue.
     *
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
     *
     * @param tower The tower that the bacteria is being moved away from.
     */
    private void moveBacteriaToNextTower(AntibioticTower tower) {
        Bacteria bacteria = bacteriaToTower.get(tower).remove();
        int nextIndex = towers.indexOf(tower) + 1;

        if (!(nextIndex > towers.size()-1)) {
            AntibioticTower next = towers.get(nextIndex);
            bacteriaToTower.get(next).add(bacteria);
        }
    }

    /**
     * Start the thread for the specified tower so that it shoots
     * at the bacteria in its range - check first that it hasn't
     * already been started. Set the flag to indicate that the
     * thread should execute.
     *
     * @param tower The tower that will begin shooting.
     */
    private void activateTower(AntibioticTower tower) {
        tower.setShooting(true);
        int index = towers.indexOf(tower);
        TowerThread t = towerThreads.get(index);
        if (!t.isAlive()) {
            t.start();
        }
    }

    /**
     * Start a thread to begin adding bacteria to the game -
     * first check that it hasn't been started yet. Set the flag
     * to indicate that the thread should execute.
     */
    private void startAddingBacteria() {
        this.addingBacteria = true;
        if (!bacteriaThread.isAlive()) {
            bacteriaThread.start();
        }

    }

    /**
     * Thread to shoot the tower.
     */
    private class TowerThread extends Thread {
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
            while (tower.getShooting()) {
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
    private class BacteriaThread extends Thread {
        /**
         * Add a bacteria every second. Check if the head of each tower's queue
         * is out of range, if so, move it to next tower.
         */
        @Override
        public void run() {
            while (addingBacteria) {
                addBacteria(BacteriaType.staph);
                for (AntibioticTower t : towers) {
                    if (!t.inRange(bacteriaToTower.get(t).peek().getX())) {
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