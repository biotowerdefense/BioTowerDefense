package cisgvsu.biotowerdefense;

import android.content.res.Resources;
import android.util.Range;

/**
 * This class models an antibiotic "tower" that shoots
 * a dosage of a certain type of antibiotic at the different
 * bacteria.
 *
 * Created by Ella on 8/31/2017.
 */

public class AntibioticTower {
    int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    int fifthWidth = screenWidth / 5;

    /** Type of antibiotic that this tower shoots. */
    private AntibioticType type;

    /** Power of each antibiotic "dosage." */
    private int power;

    /** Cost to buy this type of tower. */
    private int cost;

    /** Location of tower in game, 0 - 4 to begin with */
    private int location;
    private int minRange;
    private int maxRange;

    /** Whether or not the tower is shooting at bacteria (thread control). */
    private boolean shooting;

    /**
     * Create a default penicillin tower at the first location.
     */
    public AntibioticTower() {
        this.type = AntibioticType.penicillin;
        this.power = AntibioticType.getPower(type);
        this.cost = AntibioticType.getCost(type);
        this.location = 0;
        this.maxRange = screenWidth;
        this.minRange = screenWidth - fifthWidth;
    }

    /**
     * Create a new tower of the specified type and at the given location.
     * @param type Type of antibiotic for this tower to shoot.
     * @param location Location of tower in game.
     */
    public AntibioticTower(AntibioticType type, int location) {
        this.type = type;
        this.power = AntibioticType.getPower(type);
        this.cost = AntibioticType.getCost(type);
        this.location = location;

        //set the reach of the tower
        switch (location) {
            case 0:
                this.maxRange = screenWidth;
                this.minRange = screenWidth - fifthWidth;
            case 1:
                this.maxRange = screenWidth - fifthWidth;
                this.minRange = screenWidth - 2*fifthWidth;
            case 2:
                this.maxRange = screenWidth - 2*fifthWidth;
                this.minRange = screenWidth - 3*fifthWidth;
            case 3:
                this.maxRange = screenWidth - 3*fifthWidth;
                this.minRange = screenWidth - 4*fifthWidth;;
            case 4:
                this.maxRange = screenWidth = 4*fifthWidth;
                this.minRange = 0;
        }
    }

    /**
     * Get the type of the tower.
     * @return Antibiotic that this tower shoots.
     */
    public AntibioticType getType() {
        return type;
    }

    /**
     * Set the type of the tower.
     * @param type The new antibiotic that this tower shoots.
     */
    public void setType(AntibioticType type) {
        this.type = type;
    }

    /**
     * Get the tower's power.
     * @return Power of one antibiotic "dosage".
     */
    public int getPower() {
        return power;
    }

    /**
     * Set the power for the tower.
     * @param power New power of one antibiotic "dosage".
     */
    public void setPower(int power) {
        this.power = power;
    }

    /**
     * Get the cost of the tower.
     * @return Cost of the tower.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Set the cost of the tower.
     * @param cost New cost of the tower.
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * Get whether or not the bacteria is in range of the tower
     * @param x the x-coordinate of the bacteria
     * @return True if it is in range, false otherwise
     */
    public boolean inRange(int x) {
        if (x <= this.maxRange && x >= this.minRange) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get whether or not this tower is shooting.
     * @return True if shooting, false otherwise.
     */
    public boolean getShooting() {
        return this.shooting;
    }

    /**
     * Set whether or not this tower is shooting,
     * @param newVal New value for whether or not this tower is shooting.
     */
    public void setShooting(boolean newVal) {
        this.shooting = newVal;
    }
}