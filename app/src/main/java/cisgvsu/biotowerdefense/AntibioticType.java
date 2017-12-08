package cisgvsu.biotowerdefense;

/**
 * Created by Ella on 8/31/2017.
 * The different antibiotic types that are available in the game.
 */

public enum AntibioticType {
    penicillin, vancomycin, linezolid;

    /**
     * Get the power of one dosage of this antibiotic.
     * @param type Type of antibiotic we're checking
     * @return int representing the dosage power
     */
    public static int getPower(AntibioticType type) {
        switch (type) {
            case penicillin:
                return 1;
            case vancomycin:
                return 2;
            case linezolid:
                return 3;
            default:
                return 0;
        }
    }

    /**
     * Get the cost for this antibiotic.
     * @param type Type of antibiotic we're checking
     * @return int representing cost (will be subtracted
     * from money in game)
     */
    public static int getCost(AntibioticType type) {
        switch (type) {
            case penicillin:
                return 15;
            case vancomycin:
                return 25;
            case linezolid:
                return 35;
            default:
                return 0;
        }
    }

    /**
     * Get the antibiotic type as a string.
     * @param type
     * @return
     */
    public static String toString(AntibioticType type) {
        switch (type) {
            case penicillin:
                return "Penicillin";
            case vancomycin:
                return "Vancomycin";
            case linezolid:
                return "Linezolid";
            default:
                return null;
        }
    }

    /**
     * Get the AntibioticType enum from a string.
     * @param str String to be converted into enum
     * @return
     */
    public static AntibioticType stringToEnum(String str) {
        if (str.equalsIgnoreCase("penicillin")) {
            return penicillin;
        } else if (str.equalsIgnoreCase("vancomycin")) {
            return vancomycin;
        } else if (str.equalsIgnoreCase("linezolid")) {
            return linezolid;
        } else {
            return null;
        }
    }

    /**
     * Get the image resource for this tower
     * @param type
     * @return
     */
    public static int getImage(AntibioticType type) {
        switch (type) {
            case penicillin:
                return R.drawable.tower_penicillin;
            case vancomycin:
                return R.drawable.tower_vancomycin;
            case linezolid:
                return R.drawable.tower_linezolid;
            default:
                return R.drawable.tower_placeholder;
        }
    }

    /**
     * Get the string resource of the detail for this tower
     * @param type
     * @return
     */
    public static int getDescription(AntibioticType type) {
        switch (type) {
            case penicillin:
                return R.string.penicillin_description;
            case vancomycin:
                return R.string.vancomycin_description;
            case linezolid:
                return R.string.linezolid_description;
            default:
                return -1;
        }
    }
}