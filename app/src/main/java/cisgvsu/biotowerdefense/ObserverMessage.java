package cisgvsu.biotowerdefense;

/**
 * The object to be passed from the Observable to the Observer, with a message.
 */

public class ObserverMessage {
    /** Use to indicate that this a message telling the Observer that the game is over. */
    public static final int GAME_OVER = 0;

    /** Use to indicate that this is a message telling the Observer that a bacteria
     * has become resistant to an antibiotic. */
    public static final int RESISTANCE = 1;

    /** Type for this message. */
    private int type;

    /** Text for this message. */
    private String text;

    /**
     * Create a new message.
     * @param type Message type
     * @param text Message text
     */
    public ObserverMessage(int type, String text) {
        this.type = type;
        this.text = text;
    }

    /**
     * Get the type of this message.
     * @return
     */
    public int getType() {
        return this.type;
    }

    /**
     * Get the text for this message.
     * @return
     */
    public String getText() {
        return this.text;
    }
}
