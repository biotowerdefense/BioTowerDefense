package cisgvsu.biotowerdefense;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
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
public class Game extends Observable {
    /** The number of towers allowed in the game. */
    private static final int NUM_TOWERS = 5;
    /** List of towers. */
    private Vector<AntibioticTower> towers;
    /** Mapping of the bacteria that are in each tower's range.  */
    private ConcurrentHashMap<AntibioticTower, Queue<Bacteria>> bacteriaToTower;
    private Vector<Bacteria> unassignedBacteria;
    /** Antibiotic resistance for new bacteria. */
    private ConcurrentHashMap<BacteriaType, List<AntibioticType>> resistances;
    /** The thread that adds bacteria to the game. */
    private BacteriaThread bacteriaThread = new BacteriaThread();

    /** The set of threads that make the towers shoot. */
    private List<TowerThread> towerThreads = new ArrayList<>();

    /** Control whether the game should be running the thread to add bacteria. */
    private boolean addingBacteria;

    /** Keep track of the current score */
    private int score;

    /** For other classes to see if the game is paused or not. */
    private boolean isPaused = true;

    /** All the towers we've purchased. */
    private ArrayList<AntibioticTower> inventory;

    /**
     * Creates a new game instance by instantiating the
     * fields, making them all thread-safe.
     */
    public Game() {
        // Instantiate lists
        towers = new Vector<>(NUM_TOWERS);
        bacteriaToTower = new ConcurrentHashMap<>();
        resistances = new ConcurrentHashMap<>();
        unassignedBacteria = new Vector<>();
        inventory = new ArrayList<>();

        // Start the game
        this.startGame();
    }

    /**
     * Start the game by giving one freebie penicillin tower
     * in the inventory.
     */
    public void startGame() {
        AntibioticTower freebie = new AntibioticTower(AntibioticType.penicillin, -1);
        this.inventory.add(freebie);
    }

    /**
     * Add bacteria and make the towers shoot.
     */
    public void restartGame() {
        this.startAddingBacteria();
        if (towers != null && towers.size() > 0) {
            for (AntibioticTower t : towers) {
                this.activateTower(t);
            }
        }
        this.isPaused = false;
    }

    /**
     * Stop the towers from shooting and bacteria from
     * moving.
     */
    public void stopGame() {
        this.addingBacteria = false;
        this.isPaused = true;
        for (AntibioticTower t : towers) {
            t.setShooting(false);
        }
    }

    /**
     * Get the value of the isPaused variable.
     * @return True if paused, false otherwise.
     */
    public boolean isPaused() {
        return this.isPaused;
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
        for (Bacteria bacteria : unassignedBacteria) {
            allBacteria.add(bacteria);
        }
        return allBacteria;
    }


    /**
     * Add this tower to our inventory, location = -1 to indicate it's in inventory.
     * @param type Type of tower to buy.
     * @return The new inventory list.
     */
    public ArrayList<AntibioticTower> buyTower(AntibioticType type) {
        this.inventory.add(new AntibioticTower(type, -1));
        return this.inventory;
    }


    /**
     * Get our inventory.
     * @return The list of towers we've purchased.
     */
    public ArrayList<AntibioticTower> getInventory() {
        return this.inventory;
    }

    public ArrayList<String> getInventoryAsStrings() {
        ArrayList<String> strInventory = new ArrayList<>();
        for (AntibioticTower t : inventory) {
            String placed = "true";
            if (t.getLocation() == -1) {
                placed = "false";
            }
            String type = AntibioticType.toString(t.getType());
            Log.d("tag", type + ": " + placed);
            strInventory.add(type + ": " + placed);
        }
        Log.d("tag", "Size: " +inventory.size());
        return strInventory;
    }

    /**
     * Add a new tower to the game with the specified type and in the given
     * location, replacing any existing tower in that location and taking on
     * its list of bacteria.
     *
     * @param tower The tower we're adding
     * @return The updated list of towers in the game, null if location is invalid.
     */
    public Vector<AntibioticTower> addTower(AntibioticTower tower, int newLocation) {
        // Make sure location is valid
        if (newLocation > NUM_TOWERS - 1 || newLocation < 0) {
            return null;
        } else {
            // Get rid of tower currently at that location if it exists
            AntibioticTower oldTower = null;
            if (!towers.isEmpty() && towers.get(newLocation) != null) {
                oldTower = towers.remove(newLocation);
                oldTower.setLocation(-1); // Back into inventory

                // Stop shooting thread for this tower
                towerThreads.remove(newLocation);
                oldTower.setShooting(false);
            }

            // Make a new tower and add it to the list
            towers.add(newLocation, tower);
            towerThreads.add(newLocation, new TowerThread(tower));

            // Get any bacteria that may have belonged to the tower previously in this
            // location and remove it from the mapping
            LinkedList<Bacteria> bacteriaList = new LinkedList<>();
            if (oldTower != null) {
                bacteriaList = (LinkedList<Bacteria>) bacteriaToTower.remove(oldTower);
            }

            // Put the new tower in the mapping to its bacteria
            bacteriaToTower.put(tower, bacteriaList);
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
                //get a score bonus for killing a bacteria
                score++;
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
                    Object obj[] = bacteriaToTower.get(t).toArray();
                    for (int i = 0; i < obj.length; i++) {
                        Bacteria b = (Bacteria) obj[i];
                        if (b.getType().equals(bacteria.getType())) {
                            b.setExempt(antibiotic);
                        }
                    }
                }

                // TODO: Notify control class that bacteria became resistant
                // Call setChanged in Observable & notify observers
                setChanged();
                notifyObservers(bacteria.getType() + " has become resistant to " + antibiotic);
                Log.d("tag", "Resistant? true");
                return true;
            } else {
                Log.d("tag", "Resistant? false");
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
        double chance = 0;
        switch (antibiotic) {
            case penicillin:
                chance = 0.03;
                //chance = 0.9;
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
        if (towers.size() > 0) {
            AntibioticTower firstTower = towers.get(0);
            bacteriaToTower.get(firstTower).add(bacteria);
        } else {
            unassignedBacteria.add(bacteria);
        }
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
        } else {
            unassignedBacteria.add(bacteria);
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
     * Get the current score
     * @return current game score
     */
    public int getScore() {
        return score;
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
                //Add to score once a second while game is running (aka bacteria is being added)
                score++;
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