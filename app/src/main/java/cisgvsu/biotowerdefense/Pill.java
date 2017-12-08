package cisgvsu.biotowerdefense;

/**
 * The pills that are targeting bacteria
 * This class keeps track of where the pills are on screen
 *
 * Created by Ella on 11/19/2017.
 */

public class Pill {

    /** Pill's current x coordinate position */
    private float x;

    /** Pill's current y coordinate position */
    private float y;

    /** The Bacteria that this pill is targeting */
    private Bacteria target;

    /** The origin tower of this pill */
    private int origin;

    public Pill(int x, int y, Bacteria target, int origin) {
        this.x = x;
        this.y = y;
        this.target = target;
        this.origin = origin;
    }

    /**
     * Get the x coordinate.
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * Set the x coordinate.
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Get the y coordinate.
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     * Set the y coordinate.
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Get the target bacteria.
     * @return
     */
    public Bacteria getTargetBacteria() {
        return target;
    }

    /**
     * Get the origin.
     * @return
     */
    public int getOrigin() {
        return origin;
    }

    /**
     * Update this pill's position.
     */
    public void updatePosition() {
        switch (origin) {
            case 2:
                x -= 3;
                break;
            case 0:
            case 1:
            case 3:
            case 4:
                y += 3;
                break;
        }
    }
}
