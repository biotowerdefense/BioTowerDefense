package cisgvsu.biotowerdefense;

/**
 * Created by Kelsey on 11/21/2017.
 */

public class ObserverMessage {
    public static final int GAME_OVER = 0;
    public static final int RESISTANCE = 1;

    private int type;
    private String text;

    public ObserverMessage(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public int getType() {
        return this.type;
    }

    public String getText() {
        return this.text;
    }
}
