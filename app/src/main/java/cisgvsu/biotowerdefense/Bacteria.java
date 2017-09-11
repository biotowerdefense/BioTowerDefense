package cisgvsu.biotowerdefense;

import java.util.HashMap;

/**
 * The bacteria objects for the game.
 * Created by Kelsey on 8/31/2017.
 */
public class Bacteria {

    /** The bacteria's current health. */
    private int health;

    /** The antibiotics available and if this bacteria is resistant to them. */
    private HashMap<AntibioticType, Boolean> resistant;

    /** The type of this bacteria. */
    private BacteriaType type;

    /** Whether or not this bacteria is currently on the screen. */
    private boolean onScreen;

    /** The value to be added to the player's score when this bacteria is killed. */
    private int value;

    /**
     * Create a new bacteria with the given type and health.
     * Score value is the bacteria's initial health.
     * @param type
     * @param health
     */
    public Bacteria(BacteriaType type, int health) {
        this.type = type;
        this.health = health;
        this.value = health;
        this.onScreen = false;
        this.resistant = null;
    }

    /**
     * Get the bacteria's health.
     * @return An int representing how much health the bacteria has.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Set the bacteria's health.
     * @param health The new health of the bacteria.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Return a boolean indicating whether or not the bacteria
     * is resistant to the specific antibiotic.
     * @param antibiotic The antibiotic we're checking.
     * @return True if resistant, false otherwise.
     */
    public boolean isResistantTo(AntibioticType antibiotic) {
        return resistant.get(antibiotic);
    }

    /**
     * Set the new resistance value for the specified antibiotic.
     * @param newResistance
     */
    public void setResistant(AntibioticType antibiotic, boolean newResistance) {
        this.resistant.put(antibiotic, newResistance);
    }

    /**
     * Return the type of this bacteria.
     * @return
     */
    public BacteriaType getType() {
        return type;
    }

    /**
     * Set the type of this bacteria.
     * @param type
     */
    private void setType(BacteriaType type) {
        this.type = type;
    }

    /**
     * Return whether or not this bacteria is displayed on the screen.
     * @return
     */
    public boolean isOnScreen() {

        return onScreen;
    }

    /**
     * Set whether or not this bacteria is currently displayed on the screen.
     * @param onScreen
     */
    public void setOnScreen(boolean onScreen) {

        this.onScreen = onScreen;
    }

    /**
     * Get the score value of this bacteria.
     * @return
     */
    public int getValue() {

        return value;
    }

    /**
     * Set the score value of this bacteria.
     * @param value
     */
    public void setValue(int value) {

        this.value = value;
    }

    public boolean outOfRange() {
        return false;
    }
}
