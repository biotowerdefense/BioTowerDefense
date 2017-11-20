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

    /** Pill's current x speed */
    private float xSpeed;

    /** Pill's current y speed */
    private float ySpeed;

    /** The Bacteria that this pill is targeting */
    private Bacteria target;

    public Pill(int x, int y, Bacteria target) {
        this.x = x;
        this.y = y;
        this.xSpeed = 0;
        this.ySpeed = 0;
        this.target = target;
    }

    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }

    public Bacteria getTargetBacteria() {
        return target;
    }

    public void updatePosition() {
        //TODO EK: make this actually move the pill TOWARDS the bacteria
        //move pill toward target
        if (xSpeed == 0 && xSpeed == 0) {
            xSpeed = (x- target.getX())/3;
            ySpeed = (y- target.getY())/3;
        }
        xSpeed = xSpeed * (float) (2.5 / Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed));
        ySpeed = ySpeed * (float) (2.5 / Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed));

        x += xSpeed;
        y -= ySpeed;
    }
}
