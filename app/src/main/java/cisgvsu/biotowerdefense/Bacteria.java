package cisgvsu.biotowerdefense;

import java.util.ArrayList;

/**
 * The bacteria objects for the game.
 * Created by Kelsey on 8/31/2017.
 */
public class Bacteria {

    /** The bacteria's current health. */
    private int health;

    /** The antibiotics that this bacteria is NOT yet resistant to, even when other
     *  bacteria of the same type that are created later will be resistant to them.*/
    private ArrayList<AntibioticType> exempt;

    /** The type of this bacteria. */
    private final BacteriaType type;

    /** Whether or not this bacteria is currently on the screen. */
    private boolean onScreen;

    private boolean initialPositionSet;

    /** The value to be added to the player's score when this bacteria is killed. */
    private int value;

    private int x;
    private int y;

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
        this.exempt = null;
        this.initialPositionSet = false;
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
     * is exempt from resistant to the specific antibiotic.
     * @param antibiotic The antibiotic we're checking.
     * @return True if exempt from resistant, false otherwise.
     */
    public boolean isExempt(AntibioticType antibiotic) {
        if (exempt != null && exempt.contains(antibiotic)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set this bacteria as being exempt from resistant to the
     * specified antibiotic.
     * @param antibiotic
     */
    public void setExempt(AntibioticType antibiotic) {
        if (exempt == null) {
            exempt = new ArrayList<>();
        }
        this.exempt.add(antibiotic);
    }

    /**
     * Return the type of this bacteria.
     * @return
     */
    public BacteriaType getType() {
        return type;
    }

    /**
     * Return whether or not this bacteria is displayed on the screen.
     * @return
     */
    public boolean isOnScreen() {
        if (x < -100) {
            this.onScreen = false;
        } else {
            this.onScreen = true;
        }
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isInitialPositionSet() {
        return this.initialPositionSet;
    }

    public void setInitialPositionSet(boolean isSet) {
        this.initialPositionSet = isSet;
    }
}
